/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.NoEntryFoundExeption;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class CustomerManagerJPA implements CustomerManager {

    private CustomerEntity dummyCustomer = new CustomerEntity("email", "dummyName", "dummyPassowrd", Role.BASIC);
    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    @MuniEshopDatabase
    private EntityManager em;
    @Inject
    private Event<CustomerEntity> customerEventSrc; //TODO will be needed?

    @Override
    public void addCustomer(CustomerEntity customer) {
        log.log(Level.INFO, "Add customer: {0}", customer.toLog());
        em.persist(customer);
        log.log(Level.INFO, "Customer added: {0}", customer.toLog());
        customerEventSrc.fire(customer);
    }

    @Override
    public void update(CustomerEntity customer) {
        log.log(Level.INFO, "Update customer: {0}", customer.toLog());
        em.merge(customer);
    }

    private CustomerEntity findByEmail(String email) throws NoEntryFoundExeption {
        log.log(Level.INFO, "Find customer by email: {0}", email);
        try {
            return em.createNamedQuery("customer.findByEmail", CustomerEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            throw new NoEntryFoundExeption("Trying to verify non-existig customer", nre);
        }
    }

    @Override
    public List<CustomerEntity> getCustomers() {
        log.info("Get customers");
        return em.createNamedQuery("customer.getCustomers", CustomerEntity.class).getResultList();
    }

    @Override
    public List<CustomerEntity> findCustomersOrderedByMail() {
        log.info("Find customers ordered by mail");
        return em.createNamedQuery("customer.findCustomersOrderedByMail", CustomerEntity.class).getResultList();
    }

    @Deprecated // loosing information about customers presence in DB
    @Override
    public CustomerEntity verifyCustomer(String email, String password) throws InvalidEntryException {
        CustomerEntity customer = isRegistred(email);
        return customer.getPassword().equals(password) ? customer : null;
    }

    @Override
    public CustomerEntity isRegistred(String email) throws InvalidEntryException {
        EntityValidator<CustomerEntity> validator = new EntityValidator<CustomerEntity>();
        dummyCustomer.setEmail(email);
        boolean isValid = false;
        isValid = validator.validate(dummyCustomer);
        if (isValid) {
            try {
                return findByEmail(email);
            } catch (NoEntryFoundExeption nre) {
                return null;
            }
        } else {
            throw new IllegalStateException("FATAL: Never should get here, there is some bug!!!"); // TODO remove, debugg reasons only 
        }
    }
}