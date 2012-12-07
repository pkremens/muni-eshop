/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.security;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import java.util.Set;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.seam.security.Credentials;

/**
 *
 * @author Tomas Kodaj, Petr Kremensky <207855@mail.muni.cz>
 */
public class Authenticator extends BaseAuthenticator {

    @Inject
    private Credentials credentials;
    @Inject
    private FacesContext facesContext;
    @Inject
    @JPA
    private CustomerManager customerManager;

    // Don't look to DB until there is a chance that we will find something!
    @Override
    public void authenticate() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        CustomerEntity customer = new CustomerEntity(credentials.getUsername(), "name", "password", Role.BASIC);

        Set<ConstraintViolation<CustomerEntity>> constraintViolations =
                validator.validate(customer);
        if (!constraintViolations.isEmpty()) {
            // FAILS
        }


    }
}
