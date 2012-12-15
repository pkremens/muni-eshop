/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jms;

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
import cz.fi.muni.eshop.test.DummyMDB;
import cz.fi.muni.eshop.test.TestResources;
import cz.fi.muni.eshop.util.ControllerBean;
import cz.fi.muni.eshop.util.DataGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import javax.inject.Inject;
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
public class StoremanOrderCloseTest {

    @Inject
    private Logger log;
    @Inject
    private ProductManager productManager;
    @Inject
    private Product product;
    @Inject
    private DataGenerator dataGenerator;
    @Inject
    private ControllerBean controllerBean;
    @Inject
    private InvoiceManager invoiceManager;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private OrderManager orderManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(ControllerBean.class, InvoiceManager.class, DummyMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void storemanCloseOrderTest() {
        controllerBean.clearDB();

        //   dataGenerator.generateCustomers(1L);
        //   dataGenerator.generateProducts(1L, 10000L, 10000L); // to ensuer that every order can be closed
        //   dataGenerator.generateOrders(1L, 1L);
    }

    @Test
    //@Ignore("working")
    public void manualCloseTest() throws InterruptedException {
        customerManager.addCustomer("xxxxx@yyyyy.zz", "customer", "password");
        productManager.addProduct("product", 200L, Category.TYPE1, 100L, 0L);
        Map<Long, Long> basket = new HashMap<Long, Long>();
        log.warning(productManager.getProductByName("product").toString());
        basket.put(productManager.getProductByName("product").getId(), 5L);

        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        OrderItem orderItem = new OrderItem(productManager.getProductByName("product"), 5L);
        orderItems.add(orderItem);
        Order order = orderManager.addOrderWithOrderItems("xxxxx@yyyyy.zz", orderItems);
//        Order order = orderManager.addOrder("xxxxx@yyyyy.zz", basket);
        log.warning(order.toString());
        Thread.sleep(500);
        //     invoiceManager.closeOrder(order.getId());
    }

    @Test
    @Ignore
    public void storemanRefillTest() throws InterruptedException {
        customerManager.addCustomer("xxxxx@yyyyy.zz", "customer", "password");
        productManager.addProduct("product", 200L, Category.TYPE1, 20L, 0L);
        log.warning(productManager.getProductByName("product").toString());
        List<OrderItem> orderItems = new ArrayList<OrderItem>();
        OrderItem orderItem = new OrderItem(productManager.getProductByName("product"), 5L);
        orderItems.add(orderItem);
        Order order = orderManager.addOrderWithOrderItems("xxxxx@yyyyy.zz", orderItems);
        log.warning(order.toString());
        Thread.sleep(500); // check storeman
        //invoiceManager.closeOrder(order.getId());
    }
//    @Test
//    public void lookForClosedOrders() {
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException ex) {
//            Logger.getLogger(StoremanOrderCloseTest.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        Assert.assertTrue(invoiceManager.getInvoiceTableCount() == 10L);
//    }
}
