/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Member;
import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.basket.BasketManager;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.SetWithProducts;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
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
    @TypeResolved
    private OrderManager orderManager;
    @Inject
    private Identity identity;

    private OrderEntity newOrder;
    private static List<OrderEntity> activeOrders;
    private static List<OrderEntity> closedOrders;
    private boolean detail = false; // Show order detail
    private OrderEntity zoomOrder = null;



    @PostConstruct
    public void retrieveAllOrders() {
        log.info("POST CONSTRUCT");
        log.info("Get all orders");
        activeOrders = orderManager.getActiveOrders();
        closedOrders = orderManager.getClosedOrders();
      }
    
    public void onOrderListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final OrderEntity order) {
    	retrieveAllOrders();
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
    
    public void hideDetail() {
    	detail = false;
    	zoomOrder = null;
    }

    @Produces
    @Named("detailedLines")
    public List<OrderLineEntity> getDetails() { 
    	return zoomOrder.getOrderLines();
    }
    
    public void getOrderDetails(OrderEntity order) {
    	detail = true;
    	zoomOrder = order;
    }
    
    
    public boolean showDetail() {
    	return detail;
    }
     public boolean closeOrder(OrderEntity order) {
    	 log.info("close order");
    	 // TODO
    	 return false;
     }
}
