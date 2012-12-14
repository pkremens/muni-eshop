/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.service.OrderManager;
import java.util.Calendar;
import java.util.logging.Logger;
import javax.ejb.TransactionAttribute;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class InvoiceManagerTest {
    @Inject
    private EntityManager em;
    
    @Inject
    private Logger log;
    
    @Inject
    private OrderManager orderManager;
    

    public Invoice addInvoice(long orderId) {
        Invoice invoice = new Invoice();
        invoice.setCreationDate(Calendar.getInstance().getTime());
        Order order = orderManager.getOrderById(orderId);
        return invoice;
    }
}
