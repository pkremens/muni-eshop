/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.controller.main;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.InvoiceManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.StoremanManager;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Singleton // Nebo application scoped???
public class ControllerBean {

    @Inject
    private ProductManager productManager;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private InvoiceManager invoiceManager;
    @Inject
    private OrderManager orderManager;
    @Inject
    private StoremanManager storemanManager;
    @Inject
    private Logger log;

    public void generateData() {
    }

    @Schedule(minute = "1")
    public void controlData() {
        log.warning("Coontroll bean action log");
    }

    public void clearTables() {
        log.warning("Cleaning all tables");
        productManager.clearProductsTable();
        customerManager.clearCustomersTable();
        invoiceManager.clearInvoiceTable(); 
        orderManager.clearOrdersTable();
        storemanManager.clearStoremanTable();
    }
}
