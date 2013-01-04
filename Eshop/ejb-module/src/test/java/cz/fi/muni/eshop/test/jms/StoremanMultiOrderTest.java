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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
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
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class StoremanMultiOrderTest {

    @Inject
    private Logger log;
    @Inject
    private ProductManager productManager;
    @Inject
    private Product product;
    @Inject
    private DataGenerator dataGenerator;
    @EJB
    private Controller controllerBean;
    @EJB
    private InvoiceManager invoiceManager;
    @EJB
    private CustomerManager customerManager;


    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(Controller.class, InvoiceManager.class, StoremanMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }


    @Before
    public void storemanCloseOrderTest() {
        controllerBean.setAutoClean(false);
        controllerBean.wipeOutDb();
    }

    @Test
    public void testMultiOrderCloseNoAutoRefill() throws InterruptedException {
        dataGenerator.generateCustomers(5L);
        dataGenerator.generateProducts(5L, 200L, 1000L);
        dataGenerator.generateOrders(2L, 5L);
        Thread.sleep(500);
        Assert.assertEquals(2L, (long) invoiceManager.getInvoiceTableCount());
    }

    @Test
    public void testMultiOrderCloseAutoRefill() throws InterruptedException {
        dataGenerator.generateCustomers(20L);
        dataGenerator.generateProducts(5L, 200L, 2L, true);
        dataGenerator.generateOrders(5L, 2L);
        Thread.sleep(500);
        Assert.assertEquals(5L, (long) invoiceManager.getInvoiceTableCount());
    }

    @Test
    public void singleOrderGeneratorTest() throws InterruptedException {
        dataGenerator.generateCustomers(5L);
        dataGenerator.generateProducts(5L, 200L, 1000L);
        List<String> emails = customerManager.getCustomerEmails();
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        OrderItem orderItem = new OrderItem(productManager.getProductByName(productManager.getProductNames().get(0)), 5L);
        orderItems.add(orderItem);
        dataGenerator.generateOrder(emails.get(0), orderItems);
        Thread.sleep(500);
        Assert.assertEquals(1L, (long) invoiceManager.getInvoiceTableCount());
    }
}
