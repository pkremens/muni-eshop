package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.Controller;
import cz.fi.muni.eshop.util.DataGenerator;
import cz.fi.muni.eshop.util.Identity;

import javax.ejb.EJB;
import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.util.logging.Logger;

@Model
public class ControllerBean {

    @EJB
    private Controller controller;
    @Inject
    private Logger log;
    @Inject
    private Identity identity;
    private Long customersToGenerate = 200L;
    private Long productsToGenerate = 800L;
    @Inject
    private DataGenerator dataGenerator;
    @EJB
    private OrderManager orderManager;

    public void wipeOutDb() {
        log.info("Deleting all enties from db");
        identity.logOut();
        controller.wipeOutDb();
    }

    public void clearOrdersAndInvoices() {
        log.info("Deleting orders and invoices");
        controller.cleanInvoicesAndOrders();
        orderManager.clearOrderTable();
    }


    public void switchCleanUp() {
        log.info("switching clean up");
        controller.switchAutoClean();
    }

    public String autoCleanUpString() {
        return String.valueOf(controller.isAutoClean());
    }

    public void switchStoreman() {
        log.info("switching storeman");
        controller.switchStoreman();
    }

    public String storemanString() {
        return String.valueOf(controller.isStoreman());
    }

    public void switchJmsStoreman() {
        log.info("switching JMS storeman");
        controller.switchJmsStoreman();
    }

    public String jmsStoremanString() {
        return String.valueOf(controller.isJmsStoreman());
    }

    public Long getCustomersToGenerate() {
        return customersToGenerate;
    }

    public void setCustomersToGenerate(Long customersToGenerate) {
        this.customersToGenerate = customersToGenerate;
    }

    public Long getProductsToGenerate() {
        return productsToGenerate;
    }

    public void setProductsToGenerate(Long productsToGenerate) {
        this.productsToGenerate = productsToGenerate;
    }

    public void generateCustomers() {
        log.info("Generating customers: " + customersToGenerate.toString());
        dataGenerator.generateCustomers(customersToGenerate);
    }

    public void generateProducts() {
        log.info("Generating products: " + productsToGenerate.toString());
        dataGenerator.generateProducts(productsToGenerate, 1000L, 1000L);
    }
}
