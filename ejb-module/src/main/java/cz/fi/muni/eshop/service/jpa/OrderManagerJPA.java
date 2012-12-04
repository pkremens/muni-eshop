/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import cz.fi.muni.eshop.util.quilifier.UserDatabase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
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
public class OrderManagerJPA implements OrderManager{
    
    @Inject 
    @MyLogger
    private Logger log;
    
    @Inject 
    @UserDatabase
    private EntityManager em;
   

    @Override
    public void addOrder(OrderEntity order) {
        log.log(Level.FINE, "Adding new order: {0}", order);
        em.persist(order);
    }
  
    @Override
    public void switchOrderOpen(Long id) {
        log.log(Level.FINE, "Switching open for order with id: {0}", id);
        OrderEntity order = getOrderById(id);
        if (order.isOpenOrder()) {
            order.setOpenOrder(false);
            em.merge(order);
        } else {
            order.setOpenOrder(true);
            em.merge(order);
        }
    }

    @Override
    public OrderEntity getOrderById(Long id) {
        log.log(Level.FINE, "Get order by id: {0}", id);
        return em.createNamedQuery("order.getOrderById", OrderEntity.class).getSingleResult();
    }

    @Override
    public List<OrderEntity> getOrders() {
        log.fine("Get orders");
        return em.createNamedQuery("order.getOrders", OrderEntity.class).getResultList();
    }

    @Override
    @Produces
    @Named("activeOrders")
    public List<OrderEntity> getActiveOrders() {
        log.fine("Get active orders");
        return getOrdersByOpen(true);
    }

    @Override
    @Produces
    @Named("closedOrders")
    public List<OrderEntity> getClosedOrders() {
        log.fine("Get closed orders");
        return getOrdersByOpen(false);
    }

    private List<OrderEntity> getOrdersByOpen(boolean open) {
        log.log(Level.FINER, "Get orders by open: {0}", open);
        return em.createNamedQuery("SELECT o FROM orderEntity o WHERE o.open=:open ORDER BY o.customer,o.creationDate ASC", OrderEntity.class).getResultList();
    }

}
