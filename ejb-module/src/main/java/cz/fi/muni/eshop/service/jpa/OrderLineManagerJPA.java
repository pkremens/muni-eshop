/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.service.OrderLineManager;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;

import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
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
    @MuniEshopDatabase
    private EntityManager em;
    
    @Inject
    @MuniEshopLogger
    private Logger log;

    @Override
    public void addOrderLine(OrderLineEntity orderLine) {
        log.info("Add order line");
        em.persist(orderLine);
        log.info("Order line added");
    }

    @Override
    public List<OrderLineEntity> getOrderLines() {
        log.info("Get order lines");
        return em.createNamedQuery("orderLine.getOrderLines", OrderLineEntity.class).getResultList();
    }
}
