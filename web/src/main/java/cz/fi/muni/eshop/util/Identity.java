/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import java.io.Serializable;

import cz.fi.muni.eshop.model.Customer;
import javax.enterprise.context.SessionScoped;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@SessionScoped
public class Identity implements Serializable {

    private Customer customer = null;

    public void logIn(Customer customer) {
        if (customer != null) {
            this.customer = customer;
        }
    }

    public void logOut() {
        this.customer = null;
    }

    public boolean isLoggedIn() {
        return (customer == null ? false : true);
    }
}
