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
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.Storeman;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.StoremanManager;
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
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(StoremanManager.class,OrderManager.class,DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Storeman.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    private void setUp() {
        product = new Product("name", 5L, Category.TYPE1);
        product.setStored(5L);
        product.setReserved(6L);
        productManager.addProduct(product);
    }

    @After
    public void cleanUp() {
        productManager.clearProductsTable();
    }
    
    @Test
    public void getProductTableCountTest() {
        dataGenerator.generateProducts(20L, 20L, 20L);
        Assert.assertEquals(productManager.getProducts().size(),(long) productManager.getProductTableCount());
    }

    @Test
    public void addProductTest() {
        product = new Product("name", 5L, Category.TYPE1);
        product.setStored(5L);
        product.setReserved(6L);
        Assert.assertNull(product.getId());
        productManager.addProduct(product);
        Assert.assertNotNull(product.getId());
    }

    @Test
    public void updateTest() {
        setUp();
        long id = product.getId();
        product.setProductName("ASD");
        productManager.updateProduct(product);
        product = productManager.getProductByName("ASD");
        int hash = productManager.hashCode();
        product = productManager.getProductById(id);
        Assert.assertEquals((long) product.getId(), id);

    }

    @Test
    public void getAllTest() {
        setUp();
        product = new Product("xxx", 5L, Category.TYPE1);
        product.setStored(5L);
        product.setReserved(6L);
        productManager.addProduct(product);
        Assert.assertEquals(productManager.getProducts().size(), 2L);
        Assert.assertTrue(productManager.getProducts().contains(product));
    }
    
    
}
