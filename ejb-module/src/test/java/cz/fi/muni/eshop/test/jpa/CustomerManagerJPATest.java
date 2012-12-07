/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.Role;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.jpa.CustomerManagerJPA;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.InvalidEntryException;
import cz.fi.muni.eshop.util.NoEntryFoundExeption;
import cz.fi.muni.eshop.util.Resources;

import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.NoResultException;
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
    @MuniEshopLogger
    Logger log;
    @Inject
    private CustomerEntity customer;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "customer.war").addClasses(CustomerEntity.class, CustomerManager.class, Resources.class,
                CustomerManagerJPA.class, User.class, InvalidEntryException.class, IdentityType.class, EntityValidator.class, Role.class, NoEntryFoundExeption.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }
    @Inject
    @JPA
    CustomerManager customerManager;

    @Test
    @InSequence(1)
    public void addCustomerTest() {
        Assert.assertTrue(customerManager.getCustomers().isEmpty());
        customer = new CustomerEntity("rambo.john@foogle.com", "Rocky Balboa", "phoenix", Role.ADMIN);
        log.log(Level.INFO, "New Customer: {0}", customer.toLog());
        customerManager.addCustomer(customer);
    }

    @Test
    @InSequence(2)
    public void updateTest() throws NoEntryFoundExeption {
        Assert.assertTrue(customer.getEmail() == null);
        customer = customerManager.verifyCustomer("rambo.john@foogle.com", "phoenix");
        Assert.assertEquals("Rocky Balboa", customer.getName());
        log.info(customer.toLog());
        customer.setName("John Spartan");
        customerManager.update(customer);
        log.info(customer.toLog());
    }

    @Test
    @InSequence(3)
    public void fetchAfterUpdateTest() throws NoEntryFoundExeption {
        customer = customerManager.verifyCustomer("rambo.john@foogle.com", "phoenix");
        Assert.assertEquals("John Spartan", customer.getName());

    }

    @Test
    @InSequence(4)
    public void emailOrderingTest() {
        customer = new CustomerEntity("hallOfFame@nhl.com", "Steve Yzerman", "hattrick", Role.ADMIN);
        customerManager.addCustomer(customer);
        for (int i = 0; i < 10; i++) {
            customerManager.addCustomer(new CustomerEntity("jemail" + i + "@foogle.cz", "name" + i, "password" + i, Role.BASIC));
        }
        List<CustomerEntity> list = customerManager.findCustomersOrderedByMail();
        Assert.assertEquals("Steve Yzerman", list.get(0).getName());
        Assert.assertEquals("John Spartan", list.get(11).getName());

    }

    @Test
    @InSequence(5)
    public void enumCompareTest() {
        List<CustomerEntity> list = customerManager.findCustomersOrderedByMail();
        Assert.assertTrue(list.get(0).getRole().equals(Role.ADMIN));
        Assert.assertTrue(list.get(1).getRole().equals(Role.BASIC));
    }

    @Test(expected=NoEntryFoundExeption.class) // cannot use (expected=NoResultException.class), arquillian keep throwing java.lang.Exeption thus getting error Unexpected exception, expected<javax.persistence.NoResultException> but was<java.lang.Exception>
    @InSequence(6)
    public void verificationWrongUserTest() throws NoEntryFoundExeption {
            customerManager.verifyCustomer("Dummy", "not-important");
    }

    @Test
    @InSequence(7)
    public void verificationWrongPasswordTest() throws NoEntryFoundExeption {
        CustomerEntity nullCustomer = customerManager.verifyCustomer("hallOfFame@nhl.com", "hooray-gretzky");
        Assert.assertNull(nullCustomer);
    }

    @Test(expected = InvalidEntryException.class)
    @InSequence(8)
    public void addingCustomerWithInvalidEmailTest() throws InvalidEntryException {
        customer = new CustomerEntity("invalidMail", "name", "password", Role.ADMIN);
        EntityValidator<CustomerEntity> validator = new EntityValidator<CustomerEntity>();
        validator.validate(customer);
    }
}
