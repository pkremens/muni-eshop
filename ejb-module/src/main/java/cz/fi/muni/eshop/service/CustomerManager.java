/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class CustomerManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;

    public void addCustomer(Customer customer) {
        log.info("Adding customer: " + customer);
        em.persist(customer);
    }

    public void updateCustomer(Customer customer) {
        log.info("Updating customer: " + customer);
        em.merge(customer);
    }

    public Customer getCustomerById(Long id) {
        log.info("Find customer by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }

    public Customer getCustomerByEmail(String email) {
        log.info("Get customer by email: " + email);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Customer> getCustomers() {
        log.info("Get all customers");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer);
        return em.createQuery(criteria).getResultList();
    }

    /**
     * Use to verify customer.
     *
     * @return customer instance if email and password matches, else null
     */
    public Customer verifyCustomer(String email, String password) {
        log.info("Verify customer: email=" + email + " password=" + password);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        Predicate emailPredicate = cb.equal(customer.get("email"), email);
        Predicate passwordPredicate = cb.equal(customer.get("password"), password);
        criteria.select(customer).where(cb.and(emailPredicate, passwordPredicate));
        Customer cust = null;
        try {
            cust = em.createQuery(criteria).getSingleResult();
        } catch (NoResultException nre) {
            log.info("Unable to verify customer: email=" + email + " password=" + password);
        }
        return cust;
    }

    public void clearCustomersTable() {
        List<Customer> customers = getCustomers();
        for (Customer customer : customers) {
            em.remove(customer);
        }
    }
}
