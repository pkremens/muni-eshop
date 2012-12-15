/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.Stateless;
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

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class OrderManager {
    //order.setCreationDate(Calendar.getInstance().getTime());

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private ProductManager productManager;
    private static final int MSG_COUNT = 5; // TODO what is this for???
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    public Order addOrder(String email, List<OrderItem> orderItems) {
        Order order = new Order();
        order.setCustomer(customerManager.getCustomerByEmail(email));
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setOrderItems(orderItems);
        for (OrderItem orderItem : orderItems) {
            productManager.orderProduct(orderItem.getProduct().getId(), orderItem.getQuantity());
            em.persist(orderItem);
        }
        em.persist(order);
        noticeStoreman(order.getId());
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
            em.remove(order);
        }
    }

    public void updateOrdersInvoice(Long orderId, Long invoiceId) {
        log.info("Updating order: " + orderId + " with invoice: " + invoiceId);
        Order order = em.find(Order.class, orderId);
        Invoice invoice = em.find(Invoice.class, invoiceId);
        order.setInvoice(invoice);
        em.persist(order);
    }
}
