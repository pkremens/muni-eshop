/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.InvoiceManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Logger;

/**
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Startup
@Singleton
public class Controller {

    private boolean automatiCleanUp = true;
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
    public void init() {
        log.info("ControllerBean initialized");
    }

    public void generateData() {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(1L, 1L, 1L);
        dataGenerator.generateOrders(1L, 1L);
    }

    /**
     * Every minute clean oldest orders together with invoices
     */
    // @Schedule(minute = "*", hour = "*")
    @Schedule(minute = "*", hour = "*", second = "10,30,50")
    public void controlData() {
        if (automatiCleanUp) {
            log.info("Cleaning invoices, count=" + invoiceManager.getInvoiceTableCount().toString());
            cleanInvoicesAndOrders();
        } else {
            log.fine("Auto cleanup is turned off, invoices=" + invoiceManager.getInvoiceTableCount().toString());
        }
    }

    public void cleanInvoicesAndOrders() {
        orderManager.clearOrderTable(invoiceManager.clearInvoiceTable());
    }

    /**
     * To completely remove all data from DB
     */
    public void wipeOutDb() {
        log.info("Deleteng all entries from db");
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
        log.info("Automatic cleanUp was switched to: "
                + ((automatiCleanUp) ? "on" : "off"));
        return automatiCleanUp;
    }

    public boolean isAutoClean() {
        return automatiCleanUp;
    }

    public void setAutoClean(boolean autoClean) {
        automatiCleanUp = autoClean;
        log.info("Automatic cleanUp was switched to: "
                + ((automatiCleanUp) ? "on" : "off"));
    }

    /**
     * Switch whether storeman should be used
     *
     * @return
     */
    public boolean switchStoreman() {
        storeman = !storeman;
        log.info("Storeman service was switched to: " + ((storeman) ? "on" : "off"));
        return storeman;
    }

    public boolean isStoreman() {
        return storeman;
    }

    public void setStoreman(boolean working) {
        storeman = working;
        log.info("Storeman service was switched to: " + ((storeman) ? "on" : "off"));
    }

    /**
     * Switch between JMS storeman and direct storeman
     *
     * @return
     */
    public boolean switchJmsStoreman() {
        jmsStoreman = !jmsStoreman;
        log.info("JMS storeman service was switched to: " + ((jmsStoreman) ? "on" : "off"));
        return jmsStoreman;
    }

    public boolean isJmsStoreman() {
        return jmsStoreman;
    }

    public void setJmsStoreman(boolean working) {
        jmsStoreman = working;
        log.info("JMS storeman service was switched to: " + ((jmsStoreman) ? "on" : "off"));
    }

    /**
     * Return string with current configuration
     *
     * @return
     */
    public String report() {
        return "autoCleanUp=" + ((automatiCleanUp) ? "on" : "off") + " , storeman=" + ((storeman) ? "on" : "off") + " , JMS storeman=" + ((jmsStoreman) ? "on" : "off");
    }
}
