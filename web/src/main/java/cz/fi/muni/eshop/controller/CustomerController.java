/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.util.Identity;
import cz.fi.muni.eshop.controller.main.ControllerBean;
import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.service.CustomerManager;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Model
public class CustomerController {

    @Inject
    private ControllerBean controller;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private Logger log;
    @Inject
    private FacesContext facesContext;
    @Inject
    private Customer newCustomer;
    private List<Customer> customers;
    @Inject
    private Identity identity;

    @PostConstruct
    public void getAllCustomers() {
        customers = customerManager.getCustomers();
        initNewCustomer();
    }

    private void initNewCustomer() {
        newCustomer = new Customer();
    }

    @Produces
    @Named
    public Customer getNewCustomer() {
        return newCustomer;
    }

    @Produces
    @Named
    List<Customer> getCustomerList() {
        return customers;
    }

    public void register() {
        customerManager.addCustomer(newCustomer);
        identity.logIn(newCustomer);
    }

    public void logIn() {
        log.warning("Trying to log as: email=" + newCustomer.getEmail() + " password=" + newCustomer.getPassword());
        Customer customer = customerManager.verifyCustomer(newCustomer.getEmail(), newCustomer.getPassword());
        if (customer != null) {
            identity.logIn(newCustomer);
        } else {
            log.warning("Failed to log in");
            initNewCustomer();
        }
    }

    public void logOut() {
        identity.logOut();
        initNewCustomer();
    }
}
