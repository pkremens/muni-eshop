/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.util.Identity;
import cz.fi.muni.eshop.util.ControllerBean;
import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.ProductManager;
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

    private String email;
    private String name;
    private String password;
    @Inject
    private ControllerBean controller;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private Logger log;
    @Inject
    private FacesContext facesContext;
    @Inject
    private Identity identity;

    public void register() {
        identity.logIn(customerManager.addCustomer(email, name, password));
    }

    @Produces
    @Named
    List<Customer> getCustomerList() {
        return customerManager.getCustomers();
    }

    public void logIn() {
        identity.logIn(customerManager.verifyCustomer(email, password));
    }

    public void logOut() {
        identity.logOut();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
