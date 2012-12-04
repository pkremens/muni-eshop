/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import cz.fi.muni.eshop.util.quilifier.UserDatabase;

import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class CustomerManagerJPA implements CustomerManager {

    @Inject
    @MyLogger
    private Logger log;
    
    @Inject
    @UserDatabase
    private EntityManager em;
    
    @Inject
    private Event<CustomerEntity> customerEventSrc; //TODO will be needed?

    @Override
    public void addCustomer(CustomerEntity customer) {
        em.persist(customer);
        log.log(Level.INFO, "Adding {0}", customer.toString());
        customerEventSrc.fire(customer);
    }

    @Override
    public void update(CustomerEntity customer) {
        em.merge(customer);
    }
    
    
    // TODO NoResultExeption... catch?
    @Override
    public CustomerEntity verifyCustomer(String email, String password) {
        return em.createNamedQuery("customer.findByEmailAndPassword", CustomerEntity.class).setParameter("email", email).setParameter("password", password).getSingleResult();
    }

    @Override
    public CustomerEntity findByEmail(String email) {
       return em.createNamedQuery("customer.findByEmail", CustomerEntity.class).setParameter("email", email).getSingleResult(); //TODO Test jestli vraci null kdyz neexistuje
    }
    
    @Override
    public List<CustomerEntity> getCustomers() {
        return em.createNamedQuery("customer.getCustomers", CustomerEntity.class).getResultList();
    }

    @Override
    public List<CustomerEntity> findAllCustomersOrderByMail() {
        return em.createNamedQuery("customer.findAllCustomersOrderByMail", CustomerEntity.class).getResultList();
        
    }

}