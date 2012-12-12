/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.validator;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Role;
import cz.fi.muni.eshop.util.EntityValidator;
import cz.fi.muni.eshop.util.exceptions.InvalidEntryException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class EntityValidatorTest {

    private static EntityValidator validator;

    @Test
    public void customerValidationTest() throws InvalidEntryException {
        Customer customer = new Customer("admin0@admin.cz", "test", "test", Role.ADMIN);
        validator = new EntityValidator<Customer>();
        Assert.assertTrue(validator.validate(customer));
        customer.setEmail("aaaa");
        boolean ret = false;
        try {

            validator.validate(customer);
        } catch (InvalidEntryException ex) {
            ret = true;
        }
        Assert.assertTrue(ret);
        // TODO try some invalid data
    }

    @Test
    public void productValidationTest() throws InvalidEntryException {
        Product product = new Product("test", 8L, 0L);
        product.setId(1L);

        validator = new EntityValidator<Product>();
        Assert.assertTrue(validator.validate(product));

        // TODO some invalid data
    }

    @Test
    public void productValidationMissingIdTest() throws InvalidEntryException {
        Product product = new Product("test", 8L, 0L);
        validator = new EntityValidator<Product>();
        Assert.assertTrue(validator.validateIgnoreId(product));

        // TODO some invalid data
    }

    @Test
    public void orderValidatorTest() throws InvalidEntryException {
        Customer customer = new Customer("test@test.cz", "test", "test", Role.ADMIN);
        Product product = new Product("test", 8L);
        product.setId(3L);
        OrderItem line = new OrderItem(product, 5L);
        line.setId(4L);
        List<OrderItem> lines = new ArrayList<OrderItem>();
        lines.add(line);
        Order order = new Order(customer, lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setId(5L);
        validator = new EntityValidator<OrderItem>();
        Assert.assertTrue(validator.validate(order));
    }

    @Test
    public void orderValidatorMissingIdTest() throws InvalidEntryException {
        Customer customer = new Customer("test@test.cz", "test", "test", Role.ADMIN);
        Product product = new Product("test", 8L);
        OrderItem line = new OrderItem(product, 5L);
        List<OrderItem> lines = new ArrayList<OrderItem>();
        lines.add(line);
        Order order = new Order(customer, lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        validator = new EntityValidator<OrderItem>();
        Assert.assertTrue(validator.validateIgnoreId(order));
    }
}
