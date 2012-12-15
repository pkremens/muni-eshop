/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.logging.Logger;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Singleton // Nebo application scoped???
public class ControllerBean {
    private static long STOREMAN_REFILL=100; // to tell storeman how much to refill store

    @Inject
    private ProductManager productManager;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private OrderManager orderManager;
    @Inject
    private Logger log;
    @Inject
    private DataGenerator dataGenerator;

    public void generateData() {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(1L, 1L, 1L);
        dataGenerator.generateOrders(1L, 1L);
    }

    @Schedule(minute = "1")
    public void controlData() {
        log.warning("Coontroll bean action log");
    }

    public void clearDB() {
        log.warning("Deleteng all entries from db");
        customerManager.clearCustomersTable();
        productManager.clearProductsTable();
        if (orderManager.getOrderTableCount() > 0) {
            throw new IllegalStateException("Order table data should be deleted with customers!");
        }
    }
}
