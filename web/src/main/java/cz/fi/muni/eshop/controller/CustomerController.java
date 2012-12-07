/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.seam.security.Identity;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Named
@SessionScoped
public class CustomerController implements Serializable {

    @Inject
    @JPA
    private CustomerManager customerManager;
    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    private Identity identity;
    private CustomerEntity newCustomer;
    @Inject
    private FacesContext facesContext;
    private static List<CustomerEntity> customerList;
    private static boolean emptyCustomersList;

    @PostConstruct
    public void retrieveAllCustomers() {
        log.info("POST CONSTRUCT");
        log.info("Get all customers");
        customerList = customerManager.getCustomers();
        emptyCustomersList = customerList.isEmpty();
        initNewCustomer();
    }

    public void updateAction(CustomerEntity customer) {
        customerManager.update(customer);
    }

    public boolean isEmptyProductsList() {
        log.log(Level.INFO, "Is list of customers empty?: {0}", emptyCustomersList);
        return emptyCustomersList;
    }

    @Produces
    @Named
    public CustomerEntity getNewCustomer() {
        return newCustomer;
    }

    public void initNewCustomer() {
        newCustomer = new CustomerEntity();
    }

    public void register() throws Exception {
        if (!identity.isLoggedIn()) { // TODO ?
            newCustomer.setRole(Role.BASIC);
        }

        if (newCustomer.getPassword() == null || newCustomer.getPassword().equals("")) {
            facesContext.addMessage("addForm:password", new FacesMessage(
                    "Cannot have empty password"));
            log.info("Entered empty password");
            initNewCustomer();
        } else {
            CustomerEntity customer = customerManager.isRegistred(newCustomer.getEmail());
            if (customer == null) {
                customerManager.addCustomer(newCustomer);
                log.log(Level.INFO, "Registration: adding new customer {0}", newCustomer.toString());
                facesContext.addMessage("addForm:registerButton",
                        new FacesMessage("Customer was added"));
                initNewCustomer();
            } else {
                log.info("Registration: trying to use already registred email");
                facesContext.addMessage("addForm:registerButton",
                        new FacesMessage("User is already registered"));
                initNewCustomer();
            }

        }
    }
}
