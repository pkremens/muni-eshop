/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.InvoiceManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Startup
@Singleton
public class Controller {

    private boolean automatiCleanUp = false;
    private boolean storeman = true;
    private boolean jmsStoreman = true;
    @EJB
    private ProductManager productManager;
    @EJB
    private CustomerManager customerManager;
    @EJB
    private OrderManager orderManager;
    @EJB
    private InvoiceManager invoiceManager;
    @Inject
    private Logger log;
    @Inject
    private DataGenerator dataGenerator;

    @PostConstruct
    public void showYourselve() {
        log.warning("ControllerBean initialized");
    }

    public void generateData() {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(1L, 1L, 1L);
        dataGenerator.generateOrders(1L, 1L);
    }

    /**
     * Every minute clean oldest orders together with invoices
     */
    @Schedule(minute = "*", hour = "*")
    public void controlData() {
        if (automatiCleanUp) {
            log.warning("Cleaning invoices, count=" + invoiceManager.getInvoiceTableCount().toString());
            cleanInvoicesAndOrders();
        } else {
            log.warning("Auto cleanup is turned off, invoices=" + invoiceManager.getInvoiceTableCount().toString());
        }
    }

    public void cleanInvoicesAndOrders() {
        orderManager.clearOrderTable(invoiceManager.clearInvoiceTable());
       // productManager.unreserveProducts();
    }

    /**
     * To completely remove all data from db
     */
    public void wipeOutDb() {
        log.warning("Deleteng all entries from db");
        customerManager.clearCustomersTable();
        productManager.clearProductsTable();
       
    }

    /**
     * Switch value of automatic database cleanup.
     *
     * @return actual automatiCleanUp value.
     */
    public boolean switchAutoClean() {
        automatiCleanUp = !automatiCleanUp;
        log.warning("Automatic cleanUp was switched to: "
                + ((automatiCleanUp) ? "on" : "off"));
        return automatiCleanUp;
    }

    public boolean isAutoClean() {
        return automatiCleanUp;
    }

    public void setAutoClean(boolean autoClean) {
        automatiCleanUp = autoClean;
        log.warning("Automatic cleanUp was switched to: "
                + ((automatiCleanUp) ? "on" : "off"));
    }

    public boolean switchStoreman() {
        storeman = !storeman;
        log.warning("Storeman service was switched to: " + ((storeman) ? "on" : "off"));
        return storeman;
    }

    public boolean isStoreman() {
        return storeman;
    }

    public void setStoreman(boolean working) {
        storeman = working;
        log.warning("Storeman service was switched to: " + ((storeman) ? "on" : "off"));
    }

    public boolean switchJmsStoreman() {
        jmsStoreman = !jmsStoreman;
        log.warning("JMS storeman service was switched to: " + ((jmsStoreman) ? "on" : "off"));
        return jmsStoreman;
    }

    public boolean isJmsStoreman() {
        return jmsStoreman;
    }

    public void setJmsStoreman(boolean working) {
        jmsStoreman = working;
        log.warning("JMS storeman service was switched to: " + ((jmsStoreman) ? "on" : "off"));
    }

    public String report() {
        return "autoCleanUp=" + ((automatiCleanUp) ? "on" : "off") + " , storeman=" + ((storeman) ? "on" : "off") + " , JMS storeman=" + ((jmsStoreman) ? "on" : "off");
    }
}
