/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.util;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class NoCustomerFoundExeption extends Exception {

    public NoCustomerFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }
    
}
