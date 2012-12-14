/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.utils;

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

import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class GeneratorTest {

    @Inject
    private DataGenerator generator;
    @Inject
    private CustomerManager customerManager;
    @Inject
    private ProductManager productManager;
    @Inject
    private OrderManager orderManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "generator-test.war").addClasses(DummyMDB.class, OrderRoot.class, OrderManager.class, ProductManager.class, DataGenerator.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Test
    @InSequence(1)
    public void customerGenerationTest() {
        generator.generateCustomers(10L);
        Assert.assertEquals(customerManager.getCustomers().size(), 10);

    }

    @Test
    @InSequence(1)
    public void productsGenerationTest() {
        long price = 3L;
        generator.generateProducts(10L, price, 1000L);
        Assert.assertEquals(productManager.getProducts().size(), 10);
        for (Product product : productManager.getProducts()) {
            Assert.assertTrue("Generator generated invalid price: " + product.getPrice(), (product.getPrice() > 0L && product.getPrice() < price + 1));
        }
    }

    @Test
    @InSequence(1)
    public void productsGenerationRandomStoredTest() {
        long stored = 3L;
        productManager.clearProductsTable();
        generator.generateProducts(10L, 1L, stored, true);
        for (Product product : productManager.getProducts()) {
            Assert.assertTrue("Generator generated invalid price: " + product.getStored(), (product.getStored() > 0L && product.getStored() < stored + 1));
        }
    }

    @Test
    @InSequence(2)
    public void ordersGenerationTest() {
        generator.generateOrders(20L, 10L);
        Assert.assertEquals(orderManager.getOrders().size(), 20L);
        for (Order order : orderManager.getOrders()) {
            Assert.assertEquals(10L, order.getOrderItems().size());
        } // TODO already spent too much time!
//        orderManager.clearOrderTable();
//        long itemCount = 3L;
//        generator.generateOrders(20L, itemCount, true);
//        List<OrderItem> orderItems;
//        for (Order order : orderManager.getOrders()) {
//             Assert.assertTrue("Generator generated invalid orderItems count: " + orderItems.size(), (order.getOrderItems().size() > 0L && order.getOrderItems().size() < itemCount + 1));
//        }
    }
}
