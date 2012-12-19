/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.jms.ConnectionFactory;
import javax.jms.Queue;
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
public class InvoiceManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;
    @EJB
    private OrderManager orderManager;
    private static final int MSG_COUNT = 5; // TODO what is this for???
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;
    @EJB
    private ProductManager productManager;
//    @Inject
//    private SessionContext context;

    // used by jms
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Invoice closeOrder(Long orderId) {
        log.warning("Closing order id: " + orderId);
        Invoice invoice = new Invoice();
        Order order = orderManager.getOrderById(orderId);
        List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
        for (OrderItem orderItem : order.getOrderItems()) {
            productManager.invoiceProduct(orderItem.getProduct().getId(), orderItem.getQuantity());
            invoiceItems.add(new InvoiceItem(orderItem.getProduct(), orderItem.getQuantity()));
        }
        invoice.setInvoiceItems(invoiceItems);
        invoice.setCustomer(order.getCustomer());
        invoice.setCreationDate(Calendar.getInstance().getTime());
        invoice.setOrder(order);
        em.persist(invoice);
        orderManager.updateOrdersInvoice(order.getId(), invoice.getId());
        return invoice;
    }

    // used directly
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Invoice closeOrderDirectly(Order order) {
        log.warning("Closing order directly: " + order.getId());
        Invoice invoice = new Invoice();
        List<InvoiceItem> invoiceItems = new ArrayList<InvoiceItem>();
        for (OrderItem orderItem : order.getOrderItems()) {
            productManager.invoiceProduct(orderItem.getProduct().getId(), orderItem.getQuantity());
            invoiceItems.add(new InvoiceItem(orderItem.getProduct(), orderItem.getQuantity()));
        }
        invoice.setInvoiceItems(invoiceItems);
        invoice.setCustomer(order.getCustomer());
        invoice.setCreationDate(Calendar.getInstance().getTime());
        invoice.setOrder(order);
        em.persist(invoice);
        return invoice;
    }

    public Invoice getInvoiceById(Long id) {
        log.info("Find invoice by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteria = cb.createQuery(Invoice.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(invoice).where(cb.equal(invoice.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }

    public Invoice getInvoiceByName(String name) {
        log.info("Get invoice by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteria = cb.createQuery(Invoice.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(invoice).where(cb.equal(invoice.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Invoice> getInvoices() {
        log.info("Get all invoices");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteria = cb.createQuery(Invoice.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(invoice);
        return em.createQuery(criteria).getResultList();
    }

    public Set<Long> clearInvoiceTable() {
        Set<Long> invoiceIds = new HashSet<Long>();
        log.info("Get invoices table");
        for (Invoice invoice : getInvoices()) {
            invoiceIds.add(invoice.getOrder().getId());
            invoice.getOrder().setInvoice(null);
            em.remove(invoice);
        }
        return invoiceIds;
    }

    public Long getInvoiceTableCount() {
        log.info("Get invoices table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(cb.count(invoice));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    // can not use Ids as they are in root table: IllegalArgumentException: SingularAttribute  named id and of type java.lang.Long is not present
    public List<Invoice> getWholeInvoices() {
        List<Invoice> invoices = getInvoices();
        for (Invoice invoice : invoices) {
            Hibernate.initialize(invoice.getInvoiceItems());
            Hibernate.initialize(invoice.getOrder().getOrderItems());
        }
        return invoices;
    }

    public Invoice getWholeInvoiceById(Long id) {
        Invoice invoice = getInvoiceById(id);
        Hibernate.initialize(invoice.getInvoiceItems());
        Hibernate.initialize(invoice.getOrder().getOrderItems());
        return invoice;
    }
}