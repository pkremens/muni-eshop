package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.service.InvoiceManager;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.util.List;

@Model
public class InvoiceBean {

    @EJB
    private InvoiceManager invoiceManager;

    public List<Invoice> getInvoices() {
        return invoiceManager.getInvoices();
    }

    public void manualCloseOrder(Long orderId) {
        if (invoiceManager.manualCloseOrder(orderId) == null) {
            addMessage("This order is already closed.");
        }
    }

    private void addMessage(String summary) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN,
                summary, null);
        FacesContext.getCurrentInstance().addMessage(null, message);
    }

    public void clearInvoices() {
        invoiceManager.clearInvoiceTable();
    }

    public Long invoicesCount() {
        return invoiceManager.getInvoiceTableCount();
    }
}
