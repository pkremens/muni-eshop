/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.OrderRoot;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.test.DummyMDB;
import cz.fi.muni.eshop.test.TestResources;
import cz.fi.muni.eshop.util.DataGenerator;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class OrderTest {

    @Inject
    private Logger log;
    @Inject
    private Order order;
    @Inject
    private Customer customer;
    @Inject
    private Product product;
    @Inject
    private OrderItem orderItem;
    @Inject
    private OrderManager orderManager;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private ProductManager productManager;
    @Inject
    private DataGenerator generator;
    private long testId;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(DummyMDB.class,OrderRoot.class, DataGenerator.class, OrderManager.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                ;
    }

    private void setUp() {
        generator.generateCustomers(20L);
        generator.generateProducts(20L, 20L, 20L);
        generator.generateOrders(20L, 20L);

    }

    @Test
    @InSequence(2)
    public void addOrderTest() {
        order = new Order();
//        customer = new Customer("xxx@xxx.xx", "xxx", "xxx");
//        customerManager.addCustomer(customer);
//        order.setCustomer(customer);
//        product = new Product("ppp", 29L, Category.TYPE1, 28L, 2L);
//        productManager.addProduct(product);
//        orderItem = new OrderItem(product, 5L);
//        List<OrderItem> items = new ArrayList<OrderItem>();
//        items.add(orderItem);
//        order.setOrderItems(items);
//        orderManager.addOrder(order);
//        testId = order.getId();
//        Assert.assertNotNull(testId);
//        log.info(order.toString());

    }

    @Test
    @InSequence(1)
    public void setUpTest() {
        setUp();
    }

    @Test
    @InSequence(3)
    @Ignore
    public void updateTest() {
//
//        order = orderManager.getOrderById(463L); // TOTO MUSIM CO NEJDRIV ZMENIT AT NENI HARDCODED!!!!
//        String customersName = order.getCustomer().getName();
//        order.getCustomer().setName("xxx");
//        orderManager.update(order);
//        order = orderManager.getOrderById(testId);
//        Assert.assertEquals(order.getCustomer().getName(), "xxx");
    }
}
