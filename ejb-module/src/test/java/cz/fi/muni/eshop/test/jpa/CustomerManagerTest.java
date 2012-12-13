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
import cz.fi.muni.eshop.test.TestResources;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.InvalidEntryException;

import java.util.logging.Logger;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
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

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "customer-test.war").addClasses(EntityValidator.class,OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Storeman.class, Order.class, Customer.class ,InvalidEntryException.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    private void setUp() {
        customer = new Customer("rambo.john@foogle.com", "Rocky Balboa", "phoenix");
        customerManager.addCustomer(customer);
    }

    @After
    public void cleanUp() {
        customerManager.clearCustomersTable();
    }

    @Test
    public void addCustomerTest() {
        customer = new Customer("rambo.john@foogle.com", "Rocky Balboa", "phoenix");
        Assert.assertNull(customer.getId());
        customerManager.addCustomer(customer);
        Assert.assertNotNull(customer.getId());
    }

    @Test
    public void updateCustomerTest() {
        setUp();
        Assert.assertNotNull(customer.getId());
        customer.setEmail("xxxx@yyyy.zz");
        customerManager.updateCustomer(customer);
        Assert.assertNotNull(customerManager.getCustomerByEmail("xxxx@yyyy.zz"));
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
        customer = customerManager.verifyCustomer("rambo.john@foogle.com", "phoenix");
        Assert.assertNotNull(customer);
    }

    public void getCustomersTest() {
        setUp();
        customer = new Customer("ssss@ddd.cc", "SDSADS", "ASDSADA");
        customerManager.addCustomer(customer);
        Assert.assertEquals(customerManager.getCustomers().size(), 2);
        Assert.assertTrue(customerManager.getCustomers().contains(customer));
    }

    // OLD
    // #########################################################################
    // OLD
    @Test
    @InSequence(1)
    public void addCustomerXTest() {
        Assert.assertTrue(customerManager.getCustomers().isEmpty());
        customer = new Customer("rambo.john@foogle.com", "Rocky Balboa", "phoenix");
        Assert.assertNull(customer.getId());
        customerManager.addCustomer(customer);
        Assert.assertNotNull(customer.getId());
    }

    @Test
    @InSequence(2)
    public void updateTest(){
        setUp();
        customer = customerManager.verifyCustomer("rambo.john@foogle.com", "phoenix");
        Assert.assertEquals("Rocky Balboa", customer.getName());
        customer.setName("John Spartan");
        customerManager.updateCustomer(customer);
    }


    @Test
    @InSequence(5)
    public void verificationWrongUserTest() throws InvalidEntryException {
        Customer customer;
        customer = customerManager.verifyCustomer("Dummy", "not-important");
        Assert.assertNull(customer);
    }

    @Test // Can not expect my own exception here in Test header :/
    @InSequence(5)
    public void addingCustomerWithInvalidEmailTest() throws InvalidEntryException {
        customer = new Customer("invalidMail", "name", "password");
        EntityValidator<Customer> validator = new EntityValidator<Customer>();
        boolean caught = false;
        try {
        validator.validate(customer);
        } catch (InvalidEntryException iee) {
            caught = true;
        }
        Assert.assertTrue(caught);
        
    }
}
