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
import cz.fi.muni.eshop.util.DataGenerator;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
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
public class StoremanRefillTest {
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
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(InvoiceManager.class,StoremanMDB.class, StoremanMessage.class, OrderRoot.class, OrderManager.class, DataGenerator.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
    @Inject
    private EntityManager em;


    @Test
    public void storemanAutorefillTest() throws InterruptedException {
        Product product = productManager.addProduct("product", 4L, Category.TYPE1, 2L, 1000L);
        Assert.assertFalse(productManager.invoiceProduct(product.getId(), 4L));
        Thread.sleep(500);
        Assert.assertTrue(productManager.invoiceProduct(product.getId(), 4L));
    }
}
