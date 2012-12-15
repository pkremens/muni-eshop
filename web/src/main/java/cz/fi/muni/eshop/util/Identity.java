/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import java.io.Serializable;

import cz.fi.muni.eshop.model.Customer;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@SessionScoped
@Named
public class Identity implements Serializable {

    private Customer customer = null;
    

    /**
     * Do not allow to log in if already logged
     * @param customer
     */
    public void logIn(Customer customer) {
        if (customer != null) {
            this.customer = customer;
        }
    }
    public Customer getCustomer() {
    	return customer;
    }

    public void logOut() {
        this.customer = null;
    }

    public boolean isLoggedIn() {
        return (customer == null ? false : true);
    }
}
