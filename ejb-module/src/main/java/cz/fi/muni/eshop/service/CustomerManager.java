/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.NoEntryFoundExeption;
import java.util.List;
import javax.persistence.NoResultException;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface CustomerManager {

    void addCustomer(CustomerEntity customer);

    void update(CustomerEntity customer);

    /**
     *
     * @param email
     * @param password
     * @return instance of verified customer, return null if wrong password
     * @throws NoResultException if no customer with given ID exists
     */
    CustomerEntity verifyCustomer(String email, String password) throws NoEntryFoundExeption; // TODO Co bude lepsi? nejaky dummy dotaz kterym zjistit jeslti v DB vubec je a az pak ho kdzytak vytahnu nebo ho rovnou vytahnout 

    //CustomerEntity findByEmail(String email);
    List<CustomerEntity> getCustomers();

    List<CustomerEntity> findCustomersOrderedByMail();

    CustomerEntity isRegistred(String email) throws InvalidEntryException;

    
    
}
