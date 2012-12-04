/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.service.OrderLineManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import cz.fi.muni.eshop.util.quilifier.UserDatabase;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class OrderLineManagerJPA implements OrderLineManager {

    @Inject
    @UserDatabase
    private EntityManager em;
    
    @Inject
    @MyLogger
    private Logger log;

    @Override
    public void addOrderLine(OrderLineEntity orderLine) {
        log.fine("Add order line");
        em.persist(orderLine);
    }

    @Override
    public List<OrderLineEntity> getOrderLines() {
        log.fine("Get order lines");
        return em.createNamedQuery("orderLine.getOrderLines", OrderLineEntity.class).getResultList();
    }
}
