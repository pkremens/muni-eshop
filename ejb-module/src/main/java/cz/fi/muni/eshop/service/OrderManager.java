/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;
import org.hibernate.Hibernate;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class OrderManager {
    // order.setCreationDate(Calendar.getInstance().getTime());

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

    public Order addOrderWithMap(String email,
            Map<Long, Long> productsWithQuantity) {
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        for (Long productId : productsWithQuantity.keySet()) {
            orderItems.add(new OrderItem(productManager.getProductById(productId), productsWithQuantity.get(productId)));
        }
        return manager.addOrder(email, orderItems);
    }

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
        if (controller.isStoreman()) {
            if (controller.isJmsStoreman()) {
                noticeStoreman(order.getId());
            } else {
                log.warning("Directly (no JMS) closing order id: " + order.getId());
                invoiceManager.closeOrderDirectly(order);
            }
        }
        return order;
    }

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
            log.warning("Notifing storeman, sending order Id: " + orderId);
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

    public void updateOrdersInvoice(Long orderId, Long invoiceId) {
        log.info("Updating order: " + orderId + " with invoice: " + invoiceId);
        Order order = em.find(Order.class, orderId);
        Invoice invoice = em.find(Invoice.class, invoiceId);
        order.setInvoice(invoice);
        em.persist(order);
    }

    public Long getOrderTableCount() {
        log.info("Get orders table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(cb.count(order));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearOrderTable() {
        log.info("Clear orders ");
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

    public List<Long> getOrderIds() {
        log.info("Get all orders Ids");
        Metamodel mm = em.getMetamodel();
        EntityType<Order> mproduct = mm.entity(Order.class);
        SingularAttribute<Order, Long> id = mproduct.getDeclaredSingularAttribute("id", Long.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Order> order = criteria.from(Order.class);
        criteria.select(order.get(id));
        return em.createQuery(criteria).getResultList();
    }

    public void clearOrderTable(Set<Long> orderIds) {
        for (Long orderId : orderIds) {
            em.remove(em.find(Order.class, orderId));
        }
    }
}
