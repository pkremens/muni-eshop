package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.service.InvoiceManager;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;

@Model
public class InvoiceBean {

    @Inject
    private Logger log;
    @EJB
    private InvoiceManager invoiceManager;

    public List<Invoice> getInvoices() {
        return invoiceManager.getInvoices();
    }
    
    public void invoiceOrder(Long id) {
        invoiceManager.closeOrder(id);
    }

    public void clearInvoices() {
        invoiceManager.clearInvoiceTable();
    }
}
