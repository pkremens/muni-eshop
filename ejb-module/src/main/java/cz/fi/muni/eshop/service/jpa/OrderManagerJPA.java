/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class OrderManagerJPA implements OrderManager {

    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    @MuniEshopDatabase
    private EntityManager em;
    @Inject
    private Event<OrderEntity> orderEventSrc;

    @Override
    public void addOrder(OrderEntity order) {
        log.log(Level.INFO, "Adding new order: {0}", order);
        order.setCreationDate(Calendar.getInstance().getTime());

        em.persist(order);
        log.log(Level.INFO, "Order added: {0}", order);
        orderEventSrc.fire(order);
        log.warning("Fire event: " + order.toString());
    }

    @Override
    public void closeOrder(OrderEntity order) {
        log.warning("closing order: " + order.toString());
        order.setOpenOrder(false);


        em.merge(order);
        orderEventSrc.fire(order);
        log.warning("Fire event: " + order.toString());
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        log.log(Level.INFO, "Get order by id: {0}", id);
        return em.createNamedQuery("order.getOrderById", OrderEntity.class).getSingleResult();
    }

    @Override
    public List<OrderEntity> getOrders() {
        log.info("Get orders");
        return em.createNamedQuery("order.getOrders", OrderEntity.class).getResultList();
    }

    @Override
    @Produces
    @Named("activeOrders")
    public List<OrderEntity> getActiveOrders() {
        log.info("Get active orders");
        return getOrdersByOpen(true);
    }

    @Override
    @Produces
    @Named("closedOrders")
    public List<OrderEntity> getClosedOrders() {
        log.info("Get closed orders");
        return getOrdersByOpen(false);
    }

    private List<OrderEntity> getOrdersByOpen(boolean open) {
        log.log(Level.INFO, "Get orders by open: {0}", open);
        return em.createNamedQuery("order.getOrdersByOpen", OrderEntity.class).setParameter("open", open).getResultList();
    }
}
