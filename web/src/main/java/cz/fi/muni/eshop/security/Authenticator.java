/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.security;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.exceptions.InvalidEntryException;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;
import org.picketlink.idm.impl.api.PasswordCredential;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class Authenticator extends BaseAuthenticator {

    @Inject
    private Credentials credentials;
    @Inject
    private FacesContext facesContext;
    @Inject
    @JPA
    private CustomerManager customerManager;
    @Inject
    @MuniEshopLogger
    private Logger log;

    // Don't look to DB until there is a chance that we will find something!
    @Override
    public void authenticate() {
        String email = credentials.getUsername();
        CustomerEntity customer;
        try {
            customer = customerManager.isRegistered(email);
        } catch (InvalidEntryException iex) {
            log.log(Level.INFO, "Invalid email trying to authenticate: {0}", email);
            setStatus(AuthenticationStatus.FAILURE);
            facesContext.addMessage("loginForm:username", new FacesMessage(
                    "Invalid email, unable to authenticate")); // TODO maybe add content of constraintViolations
            return;
        }
        String password = ((PasswordCredential) credentials.getCredential()).getValue();
        if (customer == null) {
            log.log(Level.INFO, "Non-existing user trying to authenticate: {0}", email);
            setStatus(AuthenticationStatus.FAILURE);
            facesContext.addMessage("loginForm:username", new FacesMessage(
                    "Non existing user"));
        } else {
            if (customer.getPassword().equals(password)) {
                setStatus(AuthenticationStatus.SUCCESS);
                setUser(customer);
                log.log(Level.INFO, "Succesfully authenticate user: {0}", customer.toString());
            } else {
                setStatus(AuthenticationStatus.FAILURE);
                log.log(Level.WARNING, "Wrong password for user: {0}", customer.getEmail());
                FacesContext.getCurrentInstance().addMessage(
                        "loginForm:password",
                        new FacesMessage("Wrong Password"));
            }
        }
    }
}
