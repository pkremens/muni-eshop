/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

/**
 * Experienced some strange behavior with NoResultExeption in arquillian tests so introduced new Exception for non-existing entry
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class NoEntryFoundExeption extends Exception {



    public NoEntryFoundExeption(String message, Throwable cause) {
        super(message, cause);
    }

}
