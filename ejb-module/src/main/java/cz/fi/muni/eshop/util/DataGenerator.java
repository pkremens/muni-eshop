/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.Storeman;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.StoremanManager;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
// import java.util.Random;
import javax.inject.Inject;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RequestScoped // kdyz nebylo nic tak me to neslo pouzit v testech, je to OK?
public class DataGenerator {

    @Inject
    private CustomerManager customerManager;
    @Inject
    private ProductManager productManager;
    @Inject
    private OrderManager orderManager;
    @Inject
    private StoremanManager storemanManager;
    
    // private Random random = new Random();

    public void generateCustomers(Long quantity) {
        for (int i = 0; i < quantity; i++) {
            String base = "customer" + i;
            Customer customer = new Customer();
            customer.setEmail(base + "@mail.xx");
            customer.setName(base);
            customer.setPassword(base);
            customerManager.addCustomer(customer);
        }
    }

    public void generateProducts(Long quantity, Long price, Long stored) {
        for (int i = 0; i < quantity; i++) {
            String base = "product" + i;
            Product product = new Product();
            int type = (int) Math.random() * 7;
            switch (type) {
                case 0:
                    product.setCategory(Category.TYPE1);
                    break;
                case 1:
                    product.setCategory(Category.TYPE2);
                    break;
                case 2:
                    product.setCategory(Category.TYPE3);
                    break;
                case 3:
                    product.setCategory(Category.TYPE4);
                    break;
                case 4:
                    product.setCategory(Category.TYPE5);
                    break;
                case 5:
                    product.setCategory(Category.TYPE6);
                    break;
                case 6:
                    product.setCategory(Category.TYPE7);
                    break;
                default:
                    throw new IllegalStateException("Debugg me!");
            }
            product.setPrice(((long) Math.random() * price + 1) - 1);
            product.setStored(stored);
            product.setProductName(base);
            productManager.addProduct(product);
        }
    }

    /**
     * MUST BE CALLED AFTER PRODUCTS AND CUSTOMERS GENERATION!!!
     *
     * @param quantity number of orders to be generated
     */
    public void generateOrders(Long quantity, Long itemsPerOrder) {
        List<Customer> customers = customerManager.getCustomers();
        List<Product> products = productManager.getProducts();
        long productsCount = products.size();
        long customersCount = customers.size();
        OrderItem orderItem;
        Order order;
        List<OrderItem> items;
        for (int i = 0; i < quantity; i++) {
            order = new Order();
            items = new ArrayList<OrderItem>();
            order.setCustomer(customers.get((int) (Math.random() * customersCount)));
            for (int j = 0; j < itemsPerOrder; j++) {
                orderItem = new OrderItem(products.get((int) (Math.random() * productsCount)), ((long) Math.random() * quantity + 1) - 1); // don't want zeros!
                items.add(orderItem);
            }
            order.setOrderItems(items);
            orderManager.addOrder(order);
        }
    }

    public void generateStoremen(Long quantity) {
        for (int i = 0; i < quantity; i++) {
            String base = "storeman" + i;
            Storeman storeman = new Storeman(base);
            storemanManager.addProduct(storeman);
        }
    }
}
