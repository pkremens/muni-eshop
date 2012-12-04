/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.CustomerEntity;
import java.util.List;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface CustomerManager {

    void addCustomer(CustomerEntity customer);

    void update(CustomerEntity customer);

    CustomerEntity verifyCustomer(String email, String password); // TODO Co bude lepsi? nejaky dummy dotaz kterym zjistit jeslti v DB vubec je a az pak ho kdzytak vytahnu nebo ho rovnou vytahnout 

    CustomerEntity findByEmail(String email);

    List<CustomerEntity> getCustomers();

    List<CustomerEntity> findAllCustomersOrderByMail();
}
