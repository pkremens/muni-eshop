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
public class CustomerManagerTest {

    @Inject
    private CustomerManager customerManager;
    @Inject
    private Customer customer;
    @Inject
    private DataGenerator generator;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "customer-test.war").addClasses(DummyMDB.class,OrderManager.class, ProductManager.class, DataGenerator.class, OrderItem.class, Product.class, InvoiceItem.class, OrderRoot.class,Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    private void setUp() {
        customer = customerManager.addCustomer("customer@customer.xx", "cusName", "cusPas");
    }

    @After
    public void cleanUp() {
        customerManager.clearCustomersTable();
    }

    @Test
    public void getCustomerTableCountTest() {
        generator.generateCustomers(20L);
        Assert.assertEquals(customerManager.getCustomers().size(), (long) customerManager.getCustomerTableCount());
        Assert.assertEquals(20L, (long)customerManager.getCustomerTableCount());
    }

    @Test
    public void addCustomerTest() {
        Customer customer = customerManager.addCustomer("customer@customer.xx", "cusName", "cusPas");
        Assert.assertNotNull(customer.getId());
    }
    
    @Test
    public void getCustomerEmails() {
        setUp();
        customerManager.addCustomer("customer@customer.xy", "cusName", "cusPas");
        Assert.assertTrue(customerManager.getCustomerEmails().contains("customer@customer.xy"));
        Assert.assertTrue(customerManager.getCustomerEmails().contains("customer@customer.xx"));
        Assert.assertTrue(customerManager.getCustomerEmails().size()==2);
        
                
    }

    @Test
    public void updateCustomerNameTest() {
        setUp();
        customerManager.updateCustomerName("customer@customer.xx", "newName");
        customer = customerManager.getCustomerByEmail("customer@customer.xx");
        Assert.assertEquals("newName", customer.getName());
    }

    @Test
    public void getByIdTest() {
        setUp();
        long id = customer.getId();
        customer = customerManager.getCustomerById(id);
        Assert.assertNotNull(customer);
    }

    @Test
    public void verifyTest() {
        setUp();
        customer = customerManager.verifyCustomer("customer@customer.xx", "cusPas");
        Assert.assertNotNull(customer);
        customer = customerManager.verifyCustomer("xxx@yyyy.zz", "abcd");
        Assert.assertNull(customer);
    }
}
