/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import java.io.Serializable;

import cz.fi.muni.eshop.controller.BasketBean;
import cz.fi.muni.eshop.model.Customer;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@SessionScoped
@Named
public class Identity implements Serializable {

    private Customer customer = null;
    
    @Inject
    private BasketBean basket;

    /**
     * Do not allow to log in if already logged
     * @param customer
     */
    public void logIn(Customer customer) {
        if (customer != null) {
            this.customer = customer;
            basket.initNewBasket();
        }
    }
    public Customer getCustomer() {
    	return customer;
    }

    public void logOut() {
        this.customer = null;
        basket.clearBasket();
    }

    public boolean isLoggedIn() {
        return (customer == null ? false : true);
    }
    
    public String getEmail() {
    	return customer.getEmail();
    }
}
