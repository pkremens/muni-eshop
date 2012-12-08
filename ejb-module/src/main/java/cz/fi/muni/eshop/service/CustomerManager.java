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

    List<CustomerEntity> getCustomers();

    List<CustomerEntity> findCustomersOrderedByMail();
    
    /**
     * 
     * @param email
     * @param password
     * @return instance of customer if existing in DB else null
     * @throws InvalidEntryException InvalidEntryException  if entered invalid email, prevent wasting db connection resources
     */
    public CustomerEntity verifyCustomer(String email, String password) throws InvalidEntryException;
/**
 * 
 * @param email
 * @return instance of customer if existing in DB else null
 * @throws InvalidEntryException  if entered invalid email, prevent wasting db connection resources
 */
    CustomerEntity isRegistered(String email) throws InvalidEntryException;
}
