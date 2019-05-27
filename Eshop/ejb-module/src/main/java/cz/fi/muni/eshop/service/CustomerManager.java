package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
import org.hibernate.Hibernate;

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
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class CustomerManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;

    /**
     * Add new customer to DB
     *
     * @param email
     * @param name
     * @param password
     * @return instance of new created customer
     */
    public Customer addCustomer(String email, String name, String password) {
        if (getCustomerByEmailCount(email) == 1) {
            log.warning("Customer with email=" + email + " is already registered");
            return null;
        }
        Customer customer = new Customer(email, name, password);
        log.fine("Adding customer: " + customer);
        em.persist(customer);
        return customer;
    }

    /**
     * Update customers name
     *
     * @param email
     * @param name
     */
    public void updateCustomerName(String email, String name) {
        log.finer("Updating customer: email=" + email + " name=" + name);
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
        log.fine("Verify customer: email=" + email + " password=" + password);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        Predicate emailPredicate = cb.equal(customer.get("email"), email);
        Predicate passwordPredicate = cb.equal(customer.get("password"),
                password);
        criteria.select(customer).where(
                cb.and(emailPredicate, passwordPredicate));
        Customer cust = null;
        try {
            cust = em.createQuery(criteria).getSingleResult();
        } catch (NoResultException nre) {
            log.fine("Unable to verify customer: email=" + email + " password="
                    + password);
        }
        return cust;
    }

    /**
     * Return customer with given id
     *
     * @param id
     * @return customer with given id
     */
    public Customer getCustomerById(Long id) {
        log.fine("Find customer by id: " + id);
        return em.find(Customer.class, id);
    }

    /**
     * Find customer by email
     *
     * @param email
     * @return customer with given email
     */
    public Customer getCustomerByEmail(String email) {
        log.fine("Get customer by email: " + email);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer).where(cb.equal(customer.get("email"), email));
        return em.createQuery(criteria).getSingleResult();
    }

    /**
     * Get all customers
     *
     * @return all customers in DB
     */
    public List<Customer> getCustomers() {
        log.fine("Get all customers");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer);
        return em.createQuery(criteria).getResultList();
    }

    /**
     * Return id's of all customers
     *
     * @return list of customers id's
     */
    public List<Long> getCustomerIds() {
        log.fine("Get all customer Ids");
        Metamodel mm = em.getMetamodel();
        EntityType<Customer> mcustomer = mm.entity(Customer.class);
        SingularAttribute<Customer, Long> id = mcustomer.getDeclaredSingularAttribute("id", Long.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer.get(id));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * Get all emails
     *
     * @return list of all emails
     */
    public List<String> getCustomerEmails() {
        log.fine("Get all emails");
        Metamodel mm = em.getMetamodel();
        EntityType<Customer> mcustomer = mm.entity(Customer.class);
        SingularAttribute<Customer, String> email = mcustomer.getDeclaredSingularAttribute("email", String.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(customer.get(email));
        return em.createQuery(criteria).getResultList();
    }

    /**
     * Get count of customers in DB
     *
     * @return count of customers in DB
     */
    public Long getCustomerTableCount() {
        log.fine("Get customers table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Customer> customer = criteria.from(Customer.class);

        criteria.select(cb.count(customer));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    /**
     * Get number of customers with given email
     *
     * @param email
     * @return number of customers with given email
     */
    public Long getCustomerByEmailCount(String email) {
        log.fine("Get customer: " + email + " count in table");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Customer> customer = criteria.from(Customer.class);
        criteria.select(cb.count(customer)).where(cb.equal(customer.get("email"), email));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    /**
     * Clear customers table
     */
    public void clearCustomersTable() {
        log.fine("Clear customers ");
        for (Customer customer : getCustomers()) {
            em.remove(customer);
        }
    }

    /**
     * Delete customer
     *
     * @param email of customer to be deleted
     */
    public void deleteCustomer(String email) {
        Customer customer = getCustomerByEmail(email);
        em.remove(customer);
    }

    /**
     * Get random customer
     *
     * @return random instance of customer from DB
     */
    public Customer getRandomCustomer() {
        List<Customer> customers = getCustomers();
        if (customers.isEmpty()) {
            return null;
        } else {
            Random random = new Random();
            return customers.get(random.nextInt(customers.size()));
        }
    }

    public List<Order> getCustomerOrders(String email) {
        Customer customer = getCustomerByEmail(email);
        Hibernate.initialize(customer.getOrder());
        return customer.getOrder();
    }

    public Customer getWholeCustomerById(long id) {
        Customer customer = getCustomerById(id);
        Hibernate.initialize(customer.getOrder());
        for (Order order : customer.getOrder()) {
            Hibernate.initialize(order.getOrderItems());
        }
        Hibernate.initialize(customer.getInvoice());
        for (Invoice invoice : customer.getInvoice()) {
            Hibernate.initialize(invoice.getInvoiceItems());
        }
        return customer;
    }

    public Customer getWholeCustomerByEmail(String email) {
        Customer customer = getCustomerByEmail(email);
        Hibernate.initialize(customer.getOrder());
        for (Order order : customer.getOrder()) {
            Hibernate.initialize(order.getOrderItems());
        }
        Hibernate.initialize(customer.getInvoice());
        for (Invoice invoice : customer.getInvoice()) {
            Hibernate.initialize(invoice.getInvoiceItems());
        }
        return customer;
    }
}
