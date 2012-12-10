/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
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
    @TypeResolved
    private ProductManager productManager;
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

    public void onOrderListChanged(
            @Observes(notifyObserver = Reception.IF_EXISTS) final OrderEntity order) {
        log.warning("Catching event: " + order);
        retrieveAllOrders();
    }

    public void onProductListChanged(
            @Observes(notifyObserver = Reception.IF_EXISTS) final ProductEntity product) {
        log.warning("Catching event: " + product);
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
        log.info("Produces get order details " + zoomOrder.getId());
        return zoomOrder.getOrderLines();
    }

    public void getOrderDetails(OrderEntity order) {
        log.info("getOrderDetails selected: " + order.getId());
        detail = true;
        zoomOrder = order;
    }

    @Produces
    @Named("zoomPrice")
    public Long getZoomPrice() {
        return (zoomOrder == null) ? 0L : zoomOrder.getTotalPrice();

    }

    @Produces
    @Named("closableOrder")
    public boolean isOrderClosable() {
        if (!detail) {
            log.warning("Closable false because false details");
            return false;
        } else {
            boolean closable = true;
            for (OrderLineEntity orderline : zoomOrder.getOrderLines()) {
                if (orderline.getQuantity() > orderline.getProduct().getOnStore()) {
                    log.warning("We are missing this product: "
                            + orderline.getProduct().getProductName()
                            + " on store, so we can't close this order yet");
                    closable = false;

                    break; // TODO could iterate through whole list to find what
                    // else is missing
                }
            }
            log.warning("Closable = " + closable);
            return closable;
        }
    }

    public boolean showDetail() {
        log.info("show detail?");
        return detail;
    }

    public void closeOrder() {
        log.info("close order");
        orderManager.closeOrder(zoomOrder);
        productManager.updateOnStore(zoomOrder);
        hideDetail();
    }
}
