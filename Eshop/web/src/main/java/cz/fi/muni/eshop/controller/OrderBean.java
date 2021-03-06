package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.Controller;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.Identity;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

@Model
public class OrderBean {

    @Inject
    private Identity identity;
    @EJB
    private CustomerManager customerManager;
    @EJB
    private OrderManager orderManager;
    @Inject
    private DataGenerator dataGenerator;
    @EJB
    private Controller controller;
    @Inject
    private Logger log;

    public List<Order> getCustomerOrders() {
        return customerManager.getCustomerOrders(identity.getEmail());
    }
    private boolean detail = false; // Show order detail
    private Long zoomOrderId = null;
    private Long totalPrice = 0L;

    public List<OrderItem> getItemDetails() {
        totalPrice = 0L; // to ensure correct total price even if method is called more times
        List<OrderItem> items = orderManager.getOrderItemsOfOrderById(zoomOrderId);
        for (OrderItem orderItem : items) {
            totalPrice += orderItem.getQuantity()
                    * orderItem.getProduct().getPrice();
        }
        return items;
    }

    public void hideDetail() {
        detail = false;
        zoomOrderId = null;
        totalPrice = 0L;
    }

    public void showDetail(Long id) {
        zoomOrderId = id;
        detail = true;
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public Long getZoomOrderId() {
        return zoomOrderId;
    }

    public boolean isDetail() {
        return detail;
    }

    public List<Order> getOrders() {
        return orderManager.getOrders();
    }

    public void generateRanomOrder() {
        try {
            dataGenerator.generateRandomOrder();
        } catch (NullPointerException npe) {
            log.warning(npe.getMessage());
            addMessage(npe.getMessage());
        } catch (IllegalArgumentException iae) {
            log.warning(iae.getMessage());
            addMessage(iae.getMessage());
        }
    }

    public boolean isOrderClosed(Long orderId) {
        return orderManager.isOrderClosed(orderId);
    }

    private void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void clearOrders() {
        controller.cleanInvoicesAndOrders();
        orderManager.clearOrderTable();
    }
    
    public Long ordersCout() {
    	return orderManager.getOrderTableCount();
    }
}
