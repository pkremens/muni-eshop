package cz.fi.muni.eshop.service;

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
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
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
public class InvoiceManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;
    @EJB
    private OrderManager orderManager;
    @EJB
    private ProductManager productManager;

    /**
     * Close order, this method is used by StoremanMDB
     *
     * @param orderId order to be closed
     * @return invoice of closed order
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Invoice closeOrder(Long orderId) {
        log.fine("Closing order id: " + orderId);
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

    /**
     * Close order directly without JSM
     *
     * @param order
     * @return order to be closed
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED) // PAVEL
    public Invoice closeOrderDirectly(Order order) {
        log.fine("Closing order directly: " + order.getId());
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
        orderManager.updateOrdersInvoice(order.getId(), invoice.getId());
        return invoice;
    }

    /**
     * Close order, used by JSF
     *
     * @param orderId id of order which should be closed
     * @return invoice of closed order
     */
    public Invoice manualCloseOrder(Long orderId) {
        log.fine("Closing order manually id: " + orderId);
        Order order = orderManager.getOrderById(orderId);
        if (order.getInvoice() != null) {
            return null; // already closed
        }
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
        orderManager.updateOrdersInvoice(order.getId(), invoice.getId());
        return invoice;
    }

    public Invoice getInvoiceById(Long id) {
        log.fine("Find invoice by id: " + id);
        return em.find(Invoice.class, id);
    }

    public Invoice getInvoiceByName(String name) {
        log.fine("Get invoice by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteria = cb.createQuery(Invoice.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(invoice).where(cb.equal(invoice.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Invoice> getInvoices() {
        log.fine("Get all invoices");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Invoice> criteria = cb.createQuery(Invoice.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(invoice);
        return em.createQuery(criteria).getResultList();
    }

    public Set<Long> clearInvoiceTable() {
        Set<Long> invoiceIds = new HashSet<Long>();
        log.fine("Get invoices table");
        for (Invoice invoice : getInvoices()) {
            invoiceIds.add(invoice.getOrder().getId());
            invoice.getOrder().setInvoice(null);
            em.remove(invoice);
        }
        return invoiceIds;
    }

    public Long getInvoiceTableCount() {
        log.fine("Get invoices table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Invoice> invoice = criteria.from(Invoice.class);
        criteria.select(cb.count(invoice));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    // can not use Ids as they are in root table: IllegalArgumentException: SingularAttribute  named id and of type java.lang.Long is not present
    /**
     * Use to get all invoices with lazily loaded invoice items
     *
     * @return all invoices
     */
    public List<Invoice> getWholeInvoices() {
        List<Invoice> invoices = getInvoices();
        for (Invoice invoice : invoices) {
            Hibernate.initialize(invoice.getInvoiceItems());
            Hibernate.initialize(invoice.getOrder().getOrderItems());
        }
        return invoices;
    }

    /**
     * Get invoice with lazily loaded items
     * @param id of invoice to be fetched
     * @return invoice
     */
    public Invoice getWholeInvoiceById(Long id) {
        Invoice invoice = getInvoiceById(id);
        Hibernate.initialize(invoice.getInvoiceItems());
        Hibernate.initialize(invoice.getOrder().getOrderItems());
        return invoice;
    }
}