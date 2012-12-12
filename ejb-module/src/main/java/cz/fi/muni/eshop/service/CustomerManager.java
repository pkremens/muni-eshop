/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
        CriteriaQuery<Customer> criteria =  cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public Customer getProductByEmail(String email) {
        log.info("Get customer by email: " + email);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria =  cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<Customer> getCustomers() {
        log.info("Get all customers");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria =  cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer);
        return em.createQuery(criteria).getResultList();
    }
    

}
