/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Invoice;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
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
    
    
    public void addInvoice(Invoice invoice) {
        log.info("Adding invoice: " + invoice);

        em.persist(invoice);
    }
    
    public void updateInvoice(Invoice invoice) {
        log.info("Updating invoice: " + invoice);
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