/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jms;

import cz.fi.muni.eshop.jms.StoremanMDB;
import cz.fi.muni.eshop.jms.StoremanMessage;
import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.OrderRoot;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.InvoiceManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.test.TestResources;
import cz.fi.muni.eshop.util.Controller;
import cz.fi.muni.eshop.util.DataGenerator;
import javax.ejb.EJB;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class DirectCloseTest {

    @EJB
    private Controller controller;
    @Inject
    private DataGenerator dataGenerator;
    @EJB
    private OrderManager orderManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "longrun-test.war").addClasses(Controller.class, InvoiceManager.class, StoremanMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void storemanCloseOrderTest() {
        controller.wipeOutDb();
        controller.setJmsStoreman(false);
        controller.setStoreman(true);
    }

    @Test
    public void manualCloseTest() throws InterruptedException {
        controller.setAutoClean(false);
        dataGenerator.generateCustomers(10L);
        dataGenerator.generateProducts(20L, 200L, 1000L, true);
        dataGenerator.generateOrders(10L, 5L, true);
        Thread.sleep(1000);
        Assert.assertEquals(10, (long) orderManager.getOrderTableCount());
    }

    @Test
    public void manualCloseAutoWipeTest() throws InterruptedException {
        controller.setAutoClean(true);
        dataGenerator.generateCustomers(10L);
        dataGenerator.generateProducts(20L, 200L, 1000L, true);
        dataGenerator.generateOrders(10L, 5L, true);
        Thread.sleep(1000);
        Assert.assertEquals(10, (long) orderManager.getOrderTableCount());
    }
}