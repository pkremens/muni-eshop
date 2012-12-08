/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

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

import org.jboss.seam.security.Credentials;
import org.jboss.seam.security.Identity;
import org.picketlink.idm.impl.api.PasswordCredential;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Named
@SessionScoped
public class CustomerController implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3100276829805233710L; // TODO Are those mandatory?
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
    
    @Inject
    private Credentials credentials;

    @PostConstruct
    public void retrieveAllCustomers() {
        log.info("POST CONSTRUCT");
        log.info("Get all customers");
        customerList = customerManager.getCustomers();
        emptyCustomersList = customerList.isEmpty();
        initNewCustomer();
    }

    public void saveAction(CustomerEntity customer) {
        log.info("Save action");
        customer.setEditable(false);
        customerManager.update(customer);
    }

    public void editAction(CustomerEntity customer) {
        log.info("Edit action");
        customer.setEditable(true);
    }

    public boolean isEmptyCustomerList() {
        //log.log(Level.INFO, "Is list of customers empty?: {0}", emptyCustomersList);
        return emptyCustomersList;
    }

    @Produces
    @Named
    public CustomerEntity getNewCustomer() {
        return newCustomer;
    }
    @Produces
    @Named
    List<CustomerEntity> getCustomerList() {
        return customerList;
    }

    public void initNewCustomer() {
        newCustomer = new CustomerEntity();
    }

    
    /**
     * Register new Customer. Should be used only by unauthorized user. New customer is automatically logged in after registration and is given Basic permission.
     * @throws InvalidEntryException if trying to register customer with invalid email.
     */
    public void register() throws InvalidEntryException  {
    	log.info("Is logged in?: " + identity.isLoggedIn());
        if (!identity.isLoggedIn()) { // TODO BEWARE!!! not allowing to create user with higher role than BASIC for non-logged user!
        	log.warning("User not logged in, setting role to BASIC"); // TODO remove when ready, this could confuse me sometimes in near future (where are my lost permissions)
            newCustomer.setRole(Role.BASIC);
        }

        if (newCustomer.getPassword() == null || newCustomer.getPassword().equals("")) { // TODO JSF should take care
            facesContext.addMessage("addCustomerForm:password", new FacesMessage(
                    "Cannot have empty password"));
            log.info("Entered empty password");
            initNewCustomer(); // TODO really needed?
        } else {
            CustomerEntity customer = customerManager.isRegistered(newCustomer.getEmail());
            if (customer == null) {
                customerManager.addCustomer(newCustomer);
                log.log(Level.INFO, "Registration: adding new customer {0}", newCustomer.toString());
                facesContext.addMessage("addCustomerForm:registerButton",
                        new FacesMessage("Customer was registered"));
                customerList.add(newCustomer);
                emptyCustomersList=false;
                // log in new customer after registration
    			credentials.setUsername(newCustomer.getEmail());
    			PasswordCredential pc = new PasswordCredential(newCustomer.getPassword());
    			credentials.setCredential(pc);
    			identity.login();       
    			
                initNewCustomer();
            } else {
                log.info("Registration: trying to use already registered email");
                facesContext.addMessage("addCustomerForm:registerButton",
                        new FacesMessage("User with this email is already registered"));
                initNewCustomer();
            }

        }
    }
}
