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
public class GeneratorMethodsTest {

    @Inject
    private Controller controllerBean;
    @Inject
    private DataGenerator dataGenerator;
    @Inject
    private InvoiceManager invoiceManager;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private ProductManager productManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(Controller.class, InvoiceManager.class, StoremanMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void storemanCloseOrderTest() {
        controllerBean.wipeOutDb();
    }

    @Test
    public void generatorMethodsTest() throws InterruptedException {
        dataGenerator.generateCustomers(1L);
        dataGenerator.generateProducts(5L, 200L, 10L, true);
        dataGenerator.generateOrders(100L, 5L, true);
        Thread.sleep(400);
        Assert.assertEquals(100L, (long) invoiceManager.getInvoiceTableCount());
    }

    @Test
    public void generateRandomCustomerTest() {
        for (int i = 0; i < 10; i++) {
            dataGenerator.generateRandomCustomer();
        }
        Assert.assertEquals(10L,(long) customerManager.getCustomerTableCount());
    }

    @Test
    public void generateRanomProductTest() {
        for (int i = 0; i < 10; i++) {
            dataGenerator.generateRandomProduct();
        }
        Assert.assertEquals(10L,(long) productManager.getProductTableCount());
    }
}
