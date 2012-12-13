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
import cz.fi.muni.eshop.test.TestResources;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class OrderTest {

    @Inject
    private Logger log;
    @Inject
    private Order order;
    @Inject
    private Customer customer;
    @Inject
    private Product product;
    @Inject
    private OrderItem orderItem;
    @Inject
    private OrderManager orderManager;
    @Inject
    private CustomerManager customerManager;
    @Inject 
    private ProductManager productManager;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "products-test.war").addClasses(OrderManager.class, ProductManager.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Storeman.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Test
    public void addOrderTest() {
        order = new Order();
        customer = new Customer("xxx@xxx.xx", "xxx", "xxx");
        customerManager.addCustomer(customer);
        order.setCustomer(customer);
        product = new Product("ppp", 29L, Category.TYPE1, 28L, 2L);
        productManager.addProduct(product);
        orderItem = new OrderItem(product, 5L);
        List<OrderItem> items = new ArrayList<OrderItem>();
        items.add(orderItem);
        order.setOrderItems(items);
        orderManager.addOrder(order);
    }
}
