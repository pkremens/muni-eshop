/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.service.BasketManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.quilifier.SetWithProducts;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import org.jboss.seam.security.Identity;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Named
@SessionScoped
public class OrderController implements Serializable {

    @Inject
    @MuniEshopLogger
    private Logger log;
    @Inject
    @JPA
    private OrderManager orderManager;
    @Inject
    private Identity identity;
    @Inject
    @SetWithProducts
    private BasketManager basket;
    private OrderEntity newOrder;
    private static List<OrderEntity> activeOrders;
    private static List<OrderEntity> closedOrders;

    @Produces
    @Named
    public OrderEntity getNewOrder() {
        return newOrder;
    }

    @PostConstruct
    public void retrieveAllCustomers() {
        log.info("POST CONSTRUCT");
        log.info("Get all orders");
        activeOrders = orderManager.getActiveOrders();
        closedOrders = orderManager.getClosedOrders();
        initNewOrder();
    }

    @Produces
    @Named("allActiveOrders")
    public List<OrderEntity> getActiveOrders() {
        return activeOrders;
    }

    @Produces
    @Named("allClosedOrders")
    public List<OrderEntity> getClosedOrders() {
        return closedOrders;
    }

    public void initNewOrder() {
        newOrder = new OrderEntity();
    }

    public void makeOrder() { // TODO }
    }

    public void getOrderDetails() { //TODO
    }
}
