package cz.fi.muni.eshop.controller;

import java.util.List;
import java.util.logging.Logger;

import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.service.InvoiceManager;

@Model
public class InvoiceBean {
	private boolean detail = false; // Show order detail
	private Long zoomInvoice = null;
	private Long totalPrice = 0L;

	@Inject
	private Logger log;
	@Inject
	private InvoiceManager invoiceManager;

	public List<Invoice> getInvoices() {
		return invoiceManager.getInvoices();
	}

	public void hideDetail() {
		detail = false;
		zoomInvoice = null;
		totalPrice = 0L;
	}

	public void showDetail(Long id) {
		zoomInvoice = id;
		detail = true;
	}

	public boolean isDetail() {
		return detail;
	}

	@Produces
	@Named("invoiceDetails")
	public List<InvoiceItem> invoiceItems() {
	return null;	//return invoiceManager.getInvoiceItemsByInvoiceId(zoomInvoice);
	}

}
