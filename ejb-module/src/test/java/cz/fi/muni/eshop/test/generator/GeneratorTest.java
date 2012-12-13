/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.generator;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.Storeman;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.StoremanManager;
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
    
    @Inject
    private StoremanManager storemanManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "generator-test.war").addClasses(Storeman.class, StoremanManager.class,OrderManager.class,ProductManager.class, DataGenerator.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Storeman.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Test
    @InSequence(1)
    public void customerGenerationTest() {
        generator.generateCustomers(10L);
        Assert.assertEquals(customerManager.getCustomers().size(), 10);
        // TODO somehow validate data range ... or just look into logs :)
    }
    
    @Test
    @InSequence(1)
    public void productsGenerationTest() {
        generator.generateProducts(20L, 1000L, 1000L);
        Assert.assertEquals(productManager.getProducts().size(), 20);
    }
    
    @Test
    @InSequence(1)
    public void storemanGenerationTest() {
        generator.generateStoremen(20L);
        Assert.assertEquals(storemanManager.getStoremen().size(), 20L);
    }
    
    @Test
    @InSequence(2)
    public void ordersGenerationTest() {
        generator.generateOrders(20L, 10L);
         Assert.assertEquals(orderManager.getOrders().size(), 20L);
    }
}
