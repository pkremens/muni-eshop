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
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class ProductManagerTest {

    @Inject
    private Logger log;
    @Inject
    private ProductManager productManager;
    @Inject
    private Product product;
    @Inject
    private DataGenerator dataGenerator;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(DummyMDB.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private void setUp() {
        product = productManager.addProduct("name", 44L, Category.TYPE1, 343L, 23L);
    }

    @After
    public void cleanUp() {
        productManager.clearProductsTable();
    }

    @Test
    public void addProductTest() {
        setUp();
        Assert.assertNotNull(product.getId());
    }

    @Test
    public void refillProductTest() {
        setUp();
        productManager.refillProductWithReserved(product.getId(), 1000L);
        product = productManager.getProductById(product.getId());
        Assert.assertEquals(1023L, (long) product.getStored());
    }

    @Test
    public void hardRefillTest() {
        setUp();
        productManager.hardRefillProduct(product.getId(), 1000L);
        product = productManager.getProductById(product.getId());
        Assert.assertEquals(1343L, (long) product.getStored());
    }

    @Test
    public void orderProductTest() {
        setUp();
        long oldQ = product.getReserved();
        productManager.orderProduct(product.getId(), 1000L);
        product = productManager.getProductById(product.getId());
        Assert.assertEquals(1000L + oldQ, (long) product.getReserved());
    }

    @Test
    public void invoiceProductFalseTest() {

        boolean caught = false;
        try {
            product = productManager.addProduct("name", 44L, Category.TYPE1, 3L, 23L);
            productManager.invoiceProduct(product.getId(), product.getStored() + 1);
        } catch (Exception npe) { // unable to catch just NPE
            caught = true;
        }
        Assert.assertTrue(caught);
    }

    @Test
    public void invoiceProductTrueTest() {
        product = productManager.addProduct("Test", 2L, Category.TYPE1, 20L, 20L);
        productManager.invoiceProduct(product.getId(), 20L);
        product = productManager.getProductById(product.getId());
        Assert.assertEquals(0L, (long) product.getReserved());
        Assert.assertEquals(0L, (long) product.getStored());
    }

    @Test
    public void getProductTableCountTest() {
        dataGenerator.generateProducts(20L, 20L, 20L);
        Assert.assertEquals(productManager.getProducts().size(), (long) productManager.getProductTableCount());
        Assert.assertEquals(20L, (long) productManager.getProductTableCount());
    }
}
