/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa.Customer;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.jpa.CustomerManagerJPA;
import cz.fi.muni.eshop.util.Resources;

import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import java.util.List;
import java.util.logging.Level;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.picketlink.idm.api.IdentityType;
import org.picketlink.idm.api.User;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class CustomerManagerJPATest {

    @Inject
    @MyLogger
    Logger log;

    @Inject
    private CustomerEntity customer;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "customer.war").addClasses(CustomerEntity.class, CustomerManager.class, Resources.class,
                CustomerManagerJPA.class, User.class, IdentityType.class, MyLogger.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }
    
    @Inject
    @JPA
    CustomerManager customerManager;

    @Test
    @InSequence(1)
    public void addCustomerTest() {
        Assert.assertTrue(customerManager.getCustomers().isEmpty());
        customer = new CustomerEntity("rambo.john@foogle.com", "Rocky Balboa", "phoenix", "admin");
        log.log(Level.INFO, "New Customer: {0}", customer.toLog());
        customerManager.addCustomer(customer);
    }
    
    @Test
    @InSequence(2)
    public void updateTest() {
        Assert.assertTrue(customer.getEmail() == null);
        customer = customerManager.findByEmail("rambo.john@foogle.com");
        Assert.assertEquals("Rocky Balboa", customer.getName());
        log.info(customer.toLog());
        customer.setName("John Spartan");
        customerManager.update(customer);
        log.info(customer.toLog());
    }
    
    @Test
    @InSequence(3)
    public void fetchAfterUpdateTest() {
        customer = customerManager.verifyCustomer("rambo.john@foogle.com", "phoenix");
        Assert.assertEquals("John Spartan", customer.getName());
        
    }
    
    @Test
    @InSequence(4)
    public void emailOrderingTest() {
        customer = new CustomerEntity("hallOfFame@nhl.com", "Steve Yzerman", "hattrick", "admin");
        customerManager.addCustomer(customer);        
        for (int i = 0; i < 10; i++) {
            customerManager.addCustomer(new CustomerEntity("jemail" + i + "@foogle.cz", "name" + i , "password" + i, "admin"));
        }
        List<CustomerEntity> list = customerManager.findCustomersOrderedByMail();
        Assert.assertEquals("Steve Yzerman", list.get(0).getName());
        Assert.assertEquals("John Spartan", list.get(11).getName());
        
    }    
}
