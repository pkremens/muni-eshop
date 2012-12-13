/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
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
public class OrderManager {
    //order.setCreationDate(Calendar.getInstance().getTime());

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;

    public void addOrder(Order order) {
        log.info("Adding order: " + order);
        em.persist(order);
    }

    public void update(Order order) {
        log.info("Updating order: " + order);
        em.merge(order);
    }

    public Order getOrderById(Long id) {
        log.info("Get order by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(order).where(cb.equal(order.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Order> getOrders() {
        log.info("Get all orders");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(order);
        return em.createQuery(criteria).getResultList();
    }

    
    public Long getOrderTableCount() {
        log.info("Get orders table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(cb.count(order));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    
    public void clearOrdersTable() {
        for (Order order : getOrders()) {
            em.remove(order);
        }
    }
}
