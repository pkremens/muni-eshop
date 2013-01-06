package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.util.Controller;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import org.hibernate.Hibernate;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class OrderManager {

    @EJB
    private Controller controller;
    @Inject
    private EntityManager em;
    @Inject
    private Logger log;
    @EJB
    private InvoiceManager invoiceManager;
    @EJB
    private CustomerManager customerManager;
    @EJB
    private ProductManager productManager;
    @EJB
    private OrderManager manager;
    private static final int MSG_COUNT = 5;
    @Resource(mappedName = "java:/JmsXA")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    /**
     * Create new order with email and Map - product_id:count
     * @param email of customer making the order
     * @param productsWithQuantity Map of - product_id:count
     * @return newly created order
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Order addOrderWithMap(String email,
            Map<Long, Long> productsWithQuantity, Long totalPrice) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (Long productId : productsWithQuantity.keySet()) {

//            Product product = productManager.getProductById(productId);
//            THIS LINE WILL TRIGGER DEADLOCK            
//            product.addReserved(productsWithQuantity.get(productId));
                        
            orderItems.add(new OrderItem(productManager.getProductById(productId), productsWithQuantity.get(productId)));
        }
        Order order = new Order();
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setCustomer(customerManager.getCustomerByEmail(email));
        order.setOrderItems(orderItems);        
        order.setTotalPrice(totalPrice);
        em.persist(order);
        log.fine("Making order with id: " + order.getId());
        if (controller.isStoreman()) {
            if (controller.isJmsStoreman()) {
                noticeStoreman(order.getId());
            } else {
                log.fine("Directly (no JMS) closing order id: " + order.getId());
                invoiceManager.closeOrderDirectly(order);
            }
        }
        return order;
    }
    
    /**
     * Create new order with email and List of order items
     * @param email of customer making the order
     * @param orderItems List of order items
     * @return newly created order
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Order addOrder(String email, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setCustomer(customerManager.getCustomerByEmail(email));
        order.setOrderItems(orderItems);
        Long price = 0L;
        for (OrderItem orderItem : orderItems) {
            price += orderItem.getQuantity()
                    * orderItem.getProduct().getPrice();
            productManager.orderProduct(orderItem.getProduct().getId(),
                    orderItem.getQuantity());
            em.persist(orderItem);
        }
        order.setTotalPrice(price);
        em.persist(order);
        log.fine("Making order with id: " + order.getId());
        if (controller.isStoreman()) {
            if (controller.isJmsStoreman()) {
                noticeStoreman(order.getId());
            } else {
                log.fine("Directly (no JMS) closing order id: " + order.getId());
                invoiceManager.closeOrderDirectly(order);
            }
        }
        return order;
    }
    
    /**
     * Use this method to send message with order id to JMS queue
     * @param orderId id of order to be closed
     */
    private void noticeStoreman(Long orderId) {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();
            MapMessage message = session.createMapMessage();
            message.setStringProperty("type", "CLOSE_ORDER");
            log.fine("Notifing storeman, sending order Id: " + orderId);
            message.setLongProperty("orderId", orderId);
            messageProducer.send(message);
        } catch (JMSException e) {
            log.warning("A problem occurred during the delivery of this message");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    log.warning(e.getMessage());
                }
            }
        }
    }

    public boolean isOrderClosed(Long id) {
        Order order = getOrderById(id);
        return order.getInvoice() != null;
    }

    public Order getOrderById(Long id) {
        log.fine("Get order by id: " + id);
        return em.find(Order.class, id);
    }

    public List<Order> getOrders() {
        log.fine("Get all orders");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = cb.createQuery(Order.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(order);
        return em.createQuery(criteria).getResultList();
    }

    public void updateOrdersInvoice(Long orderId, Long invoiceId) {
        log.fine("Updating order: " + orderId + " with invoice: " + invoiceId);
        Order order = em.find(Order.class, orderId);
        Invoice invoice = em.find(Invoice.class, invoiceId);
        order.setInvoice(invoice);
        em.persist(order);
    }

    public Long getOrderTableCount() {
        log.fine("Get orders table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(cb.count(order));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearOrderTable() {
        log.fine("Clear orders ");
        for (Order order : getOrders()) {
            order.setCustomer(null);
            em.remove(order);
        }
    }

    public List<OrderItem> getOrderItemsOfOrderById(Long orderId) {
        Order order = getOrderById(orderId);
        Hibernate.initialize(order.getOrderItems());
        return order.getOrderItems();
    }

    public void clearOrderTable(Set<Long> orderIds) {
        for (Long orderId : orderIds) {
            em.remove(em.find(Order.class, orderId));
        }
    }
    
    /**
     * Get all orders with lazily loaded order items
     * @return list of orders
     */
    public List<Order> getWholeOrders() {
        List<Order> orders = getOrders();
        for (Order order : orders) {
            Hibernate.initialize(order.getOrderItems());
        }
        return orders;
    }
    
    /**
     * Get order with lazily loaded order items
     * @param id of order to be found
     * @return order with given id
     */
    public Order getWholeOrderById(Long id) {
        Order order = getOrderById(id);
        Hibernate.initialize(order.getOrderItems());
        return order;
    }
}
