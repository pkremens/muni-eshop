/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.utils;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class GeneratorMethodsTest {

    @EJB
    private Controller controllerBean;
    @Inject
    private DataGenerator dataGenerator;
    @EJB
    private InvoiceManager invoiceManager;
    @EJB
    private CustomerManager customerManager;
    @EJB
    private ProductManager productManager;
    @EJB
    private OrderManager orderManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(OrderManager.class, Controller.class, InvoiceManager.class, StoremanMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void storemanCloseOrderTest() {
        controllerBean.wipeOutDb();
    }

    @Test
    public void generatorMethodsProductsTest() throws InterruptedException {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(5L, 200L, 10L, true);
        dataGenerator.generateOrders(100L, 5L, true);
        Thread.sleep(1000);
        Assert.assertEquals(100L, (long) invoiceManager.getInvoiceTableCount());
    }

    @Test
    @Ignore
    public void generateRandomCustomerTest() {
        for (int i = 0; i < 10; i++) {
            dataGenerator.generateRandomCustomer();
        }
        Assert.assertEquals(10L, (long) customerManager.getCustomerTableCount());
    }

    @Test
    @Ignore
    public void generateRanomProductTest() {
        for (int i = 0; i < 10; i++) {
            dataGenerator.generateRandomProduct();
        }
        Assert.assertEquals(10L, (long) productManager.getProductTableCount());
    }

    @Test
    @Ignore
    public void testMultiOrderCloseNoAutoRefill() throws InterruptedException {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(1L, 1L, 0L);
        dataGenerator.generateOrders(1L, 1L);
        Thread.sleep(500); // Must give time to Hornet
        Assert.assertEquals(1L, (long) invoiceManager.getInvoiceTableCount());
        System.out.println("test");
        invoiceManager.clearInvoiceTable();
        Assert.assertEquals(0L, (long) invoiceManager.getInvoiceTableCount());
        orderManager.clearOrderTable();
        Assert.assertEquals(0L, (long) orderManager.getOrderTableCount());
    }
}
