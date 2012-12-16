/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.validator;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import junit.framework.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public class EntityValidatorTest {

    
    @Ignore
    @Test
    public void ignoreMe() {
        
    }
    private static EntityValidator validator;

    @Test
    public void customerValidationTest() {
        Customer customer = new Customer("admin0@admin.cz", "test", "test");
        validator = new EntityValidator<Customer>();
        Assert.assertTrue(validator.validate(customer).isEmpty());
        customer.setEmail("aaaa");
        boolean ret = false;
        Assert.assertFalse(validator.validate(customer).isEmpty());
    }

    @Test
    public void productValidationTest() {
        Product product = new Product("test", 8L, Category.TYPE1);
        product.setId(1L);
        product.setStored(4L);
        product.setReserved(5L);
        validator = new EntityValidator<Product>();
        Assert.assertTrue(validator.validate(product).isEmpty());
    }

    @Test
    public void productValidationMissingIdTest() {
        Product product = new Product("test", 8L, Category.TYPE1);
        product.setStored(4L);
        product.setReserved(5L);
        validator = new EntityValidator<Product>();
        Assert.assertTrue(validator.validateIgnoreId(product).isEmpty());

        // TODO some invalid data
    }

    @Test
    public void orderValidatorTest()  {
        Customer customer = new Customer("test@test.cz", "test", "test");
        Product product = new Product("test", 8L, Category.TYPE1);
        product.setId(3L);
        OrderItem line = new OrderItem(product, 5L);
        line.setId(4L);
        List<OrderItem> lines = new ArrayList<OrderItem>();
        lines.add(line);
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setId(5L);
        validator = new EntityValidator<OrderItem>();
        Assert.assertTrue(validator.validate(order).isEmpty());
    }

    @Test
    public void orderValidatorMissingIdTest()  {
        Customer customer = new Customer("test@test.cz", "test", "test");
        Product product = new Product("test", 8L, Category.TYPE1);
        OrderItem line = new OrderItem(product, 5L);
        List<OrderItem> lines = new ArrayList<OrderItem>();
        lines.add(line);
        Order order = new Order();
        order.setCustomer(customer);
        order.setOrderItems(lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        validator = new EntityValidator<OrderItem>();
        Assert.assertTrue(validator.validateIgnoreId(order).isEmpty());
    }
}
