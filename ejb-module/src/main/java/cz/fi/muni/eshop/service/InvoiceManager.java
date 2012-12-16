/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.Resource;
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
    @Inject
    private OrderManager orderManager;
    private static final int MSG_COUNT = 5; // TODO what is this for???
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;
    @Inject
    private ProductManager productManager;
//    @Inject
//    private SessionContext context;

    @TransactionAttribute(TransactionAttributeType.REQUIRED) 
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
        log.warning(invoice.toString());
        em.persist(invoice);
        orderManager.updateOrdersInvoice(order.getId(), invoice.getId());
        return invoice;
        //       } catch (Exception ex) {
//            context.setRollbackOnly();
        //         return null;
        //    }
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

    public void clearInvoiceTable() {
        log.info("Get invoices table");
        for (Invoice invoice : getInvoices()) {
            em.remove(invoice);
        }
    }

    public Long getInvoiceTableCount() {
        log.info("Get invoices table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(cb.count(invoice));
        return em.createQuery(criteria).getSingleResult().longValue();
    }
}