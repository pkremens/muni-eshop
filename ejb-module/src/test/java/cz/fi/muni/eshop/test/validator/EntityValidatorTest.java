/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.validator;

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
//    private static EntityValidator validator;
//
//    @Test
//    public void customerValidationTest() throws InvalidEntryException {
//        Customer customer = new Customer("admin0@admin.cz", "test", "test");
//        validator = new EntityValidator<Customer>();
//        Assert.assertTrue(validator.validate(customer));
//        customer.setEmail("aaaa");
//        boolean ret = false;
//        try {
//
//            validator.validate(customer);
//        } catch (InvalidEntryException ex) {
//            ret = true;
//        }
//        Assert.assertTrue(ret);
//        // TODO try some invalid data
//    }
//
//    @Test
//    public void productValidationTest() throws InvalidEntryException {
//        Product product = new Product("test", 8L, Category.TYPE1);
//        product.setId(1L);
//        product.setStored(4L);
//        product.setReserved(5L);
//        validator = new EntityValidator<Product>();
//        Assert.assertTrue(validator.validate(product));
//    }
//
//    @Test
//    public void productValidationMissingIdTest() throws InvalidEntryException {
//        Product product = new Product("test", 8L, Category.TYPE1);
//        product.setStored(4L);
//        product.setReserved(5L);
//        validator = new EntityValidator<Product>();
//        Assert.assertTrue(validator.validateIgnoreId(product));
//
//        // TODO some invalid data
//    }
//
//    @Test
//    public void orderValidatorTest() throws InvalidEntryException {
//        Customer customer = new Customer("test@test.cz", "test", "test");
//        Product product = new Product("test", 8L, Category.TYPE1);
//        product.setId(3L);
//        OrderItem line = new OrderItem(product, 5L);
//        line.setId(4L);
//        List<OrderItem> lines = new ArrayList<OrderItem>();
//        lines.add(line);
//        Storeman storeman = new Storeman();
//        Order order = new Order(customer, storeman);
//        order.setCreationDate(Calendar.getInstance().getTime());
//        order.setId(5L);
//        validator = new EntityValidator<OrderItem>();
//        Assert.assertTrue(validator.validate(order));
//    }
//
//    @Test
//    public void orderValidatorMissingIdTest() throws InvalidEntryException {
//        Customer customer = new Customer("test@test.cz", "test", "test");
//        Product product = new Product("test", 8L, Category.TYPE1);
//        OrderItem line = new OrderItem(product, 5L);
//        List<OrderItem> lines = new ArrayList<OrderItem>();
//        lines.add(line);
//        Storeman storeman = new Storeman();
//        Order order = new Order(customer, storeman);
//        order.setCreationDate(Calendar.getInstance().getTime());
//        validator = new EntityValidator<OrderItem>();
//        Assert.assertTrue(validator.validateIgnoreId(order));
//    }
}