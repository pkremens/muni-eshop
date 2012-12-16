package cz.fi.muni.eshop.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.inject.Inject;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.service.InvoiceManager;

@Model
public class InvoiceBean {

	@Inject
	private Logger log;
	@Inject
	private InvoiceManager invoiceManager;

	public List<Invoice> getInvoices() {
		return invoiceManager.getInvoices();
	}

	public void clearInvoices() {
		invoiceManager.clearInvoiceTable();
	}

}
