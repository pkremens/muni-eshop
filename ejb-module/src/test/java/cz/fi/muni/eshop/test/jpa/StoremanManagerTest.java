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
import java.util.List;
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
public class StoremanManagerTest {

    @Inject
    private Logger log;
    @Inject
    private Storeman storeman;
    @Inject
    private StoremanManager storemanManager;
    @Inject
    private DataGenerator dataGenerator;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(OrderManager.class, Storeman.class, StoremanManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Storeman.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    private void setUp() {
        storeman = new Storeman("dummy");
        storemanManager.addProduct(storeman);
    }

    @After
    public void cleanUp() {
        storemanManager.clearStoremanTable();
    }

    @Test
    public void getStoremanTableCountTest() {
        dataGenerator.generateStoremen(20L);
        Assert.assertEquals(storemanManager.getStoremen().size(), (long) storemanManager.getStoremanTableCount());
    }

    @Test
    public void addStoremanTest() {
        storeman = new Storeman("test");
        storemanManager.addProduct(storeman);
        Assert.assertNotNull(storeman.getId());
    }

    @Test
    public void updateTest() {
        setUp();
        long id = storeman.getId();
        storeman.setName("xxx");
        storemanManager.updateStoreman(storeman);
        storeman = storemanManager.getStoremanByName("xxx");
        Assert.assertEquals((long) storeman.getId(), id);


    }

    @Test
    public void getAllTest() {
        setUp();
        storeman = new Storeman("Test2");
        storemanManager.addProduct(storeman);
        Assert.assertEquals(storemanManager.getStoremen().size(), 2L);
        List<Storeman> storemen = storemanManager.getStoremen();
        log.info("getAllTest");
        for (Storeman storeman1 : storemen) {
            log.info(storeman1.getName());
        }
        for (Storeman storeman1 : storemen) {
            log.info(storeman1.getId().toString());
        }
//        for (Storeman storeman1 : storemen) {
//            log.info(String.valueOf(storeman1.getOrder().size()));
//        }
        
//        for (Storeman storeman1 : storemen) {
//            log.info(String.valueOf(storeman1.getInvoice().size()));
//        }


        // Assert.assertTrue(storemanManager.getStoremen().contains(storeman));
    }
}