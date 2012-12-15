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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

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

    public Customer addCustomer(String email, String name, String password) {
        Customer customer = new Customer(email, name, password);
        log.info("Adding customer: " + customer);
        em.persist(customer);
        return customer;
    }

    public void updateCustomerName(String email, String name) {
        log.info("Updating customer: email=" + email + " name=" + name);
        Customer customer = getCustomerByEmail(email);
        customer.setName(name);
        em.merge(customer);
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

    public Customer getCustomerById(Long id) {
        log.info("Find customer by id: " + id);  // tady by prece stacilo em.find ... pak to musim vsude predelat..
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
    
        public List<Long> getCustomerIds() {
        log.info("Get all customer Ids");
        Metamodel mm = em.getMetamodel();
        EntityType<Customer> mcustomer = mm.entity(Customer.class);
        SingularAttribute<Customer, Long> id =
                mcustomer.getDeclaredSingularAttribute("id", Long.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer.get(id));
        return em.createQuery(criteria).getResultList();
    }

    public List<String> getCustomerEmails() {
        log.info("Get all emails");
        Metamodel mm = em.getMetamodel();
        EntityType<Customer> mcustomer = mm.entity(Customer.class);
        SingularAttribute<Customer, String> email =
                mcustomer.getDeclaredSingularAttribute("email", String.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer.get(email));
        return em.createQuery(criteria).getResultList();
    }

    public Long getCustomerTableCount() {
        log.info("Get customers table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Customer> customer = criteria.from(Customer.class);

        criteria.select(cb.count(customer));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearCustomersTable() {
        log.info("Clear customers ");
        for (Customer customer : getCustomers()) {
            em.remove(customer);
        }
    }

	public void deleteCustomer(String email) {
		Customer customer = getCustomerByEmail(email);
		em.remove(customer);
		
	}
}
