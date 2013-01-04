package cz.fi.muni.eshop.rest;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.dao.CustomerDao;
import cz.fi.muni.eshop.model.dao.CustomerLiteDao;
import cz.fi.muni.eshop.model.dao.InvoiceDao;
import cz.fi.muni.eshop.service.InvoiceManager;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Path("/invoices")
@RequestScoped
public class InvoiceResourcesRESTService {

    @Inject
    private Logger log;
    @EJB
    private InvoiceManager invoiceManager;

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public List<InvoiceDao> listAllInvoices() {
        log.info("Get invoices");
        List<InvoiceDao> invoices = new ArrayList<InvoiceDao>();
        for (Invoice invoice : invoiceManager.getWholeInvoices()) {
            invoices.add(new InvoiceDao(invoice));
        }
        return invoices;
    }

    @GET
    @Path("/{id:[0-9][0-9]*}")
    @Produces(MediaType.APPLICATION_XML)
    public InvoiceDao lookupInvoiceById(@PathParam("id") long id) {
        return new InvoiceDao(invoiceManager.getWholeInvoiceById(id));
    }
}
