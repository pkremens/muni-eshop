/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.validator;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.util.Role;
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
        CustomerEntity customer = new CustomerEntity("admin0@admin.cz", "test", "test", Role.ADMIN);
        validator = new EntityValidator<CustomerEntity>();
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
        ProductEntity product = new ProductEntity("test", 8L, 0L);
        product.setId(1L);

        validator = new EntityValidator<ProductEntity>();
        Assert.assertTrue(validator.validate(product));

        // TODO some invalid data
    }

    @Test
    public void productValidationMissingIdTest() throws InvalidEntryException {
        ProductEntity product = new ProductEntity("test", 8L, 0L);
        validator = new EntityValidator<ProductEntity>();
        Assert.assertTrue(validator.validateIgnoreId(product));

        // TODO some invalid data
    }

    @Test
    public void orderValidatorTest() throws InvalidEntryException {
        CustomerEntity customer = new CustomerEntity("test@test.cz", "test", "test", Role.ADMIN);
        ProductEntity product = new ProductEntity("test", 8L);
        product.setId(3L);
        OrderLineEntity line = new OrderLineEntity(product, 5L);
        line.setId(4L);
        List<OrderLineEntity> lines = new ArrayList<OrderLineEntity>();
        lines.add(line);
        OrderEntity order = new OrderEntity(customer, lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        order.setId(5L);
        validator = new EntityValidator<OrderLineEntity>();
        Assert.assertTrue(validator.validate(order));
    }

    @Test
    public void orderValidatorMissingIdTest() throws InvalidEntryException {
        CustomerEntity customer = new CustomerEntity("test@test.cz", "test", "test", Role.ADMIN);
        ProductEntity product = new ProductEntity("test", 8L);
        OrderLineEntity line = new OrderLineEntity(product, 5L);
        List<OrderLineEntity> lines = new ArrayList<OrderLineEntity>();
        lines.add(line);
        OrderEntity order = new OrderEntity(customer, lines);
        order.setCreationDate(Calendar.getInstance().getTime());
        validator = new EntityValidator<OrderLineEntity>();
        Assert.assertTrue(validator.validateIgnoreId(order));
    }
}
