/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
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

    // private Random random = new Random();
    public void generateCustomers(Long quantity) {
        for (int i = 0; i < quantity; i++) {
            String base = "customer" + i;
            Customer customer = new Customer();
            customer.setEmail(base + "@mail.xx");
            customer.setName(base);
            customer.setPassword(base);
            customerManager.addCustomer(customer.getEmail(), customer.getName(), customer.getPassword());
        }
    }

    public void generateProducts(Long quantity, Long price, Long stored) {
        generateProducts(quantity, price, stored, false);
    }

    public void generateProducts(Long quantity, Long price, Long stored, boolean randomStored) {
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
            product.setPrice(((long) (Math.random() * (price - 1))) + 1);// we don't want 0 as price // mel sem tu brouka, proto tolik zavorek
            if (!randomStored) {
                product.setStored(stored);
            } else {
                product.setStored(((long) (Math.random() * (stored - 1))) + 1);
            }
            product.setProductName(base);
            productManager.addProduct(product.getProductName(), product.getPrice(), product.getCategory(), product.getStored(), product.getReserved());
        }
    }

    /**
     * MUST BE CALLED AFTER PRODUCTS AND CUSTOMERS GENERATION!!!
     *
     * @param quantity number of orders to be generated
     */
    public void generateOrders(Long quantity, Long itemsPerOrder) {
        generateOrders(quantity, itemsPerOrder, true);
    }

    public void generateOrders(long quantity, long itemCount, boolean randomItems) {
        List<String> emails = customerManager.getCustomerEmails();
        List<String> productNames = productManager.getProductNames();
        OrderItem orderItem;
        Order order;
        String email;
        String productName;
        List<OrderItem> orderItems;
        long itemsPerOrder;
        for (int i = 0; i < quantity; i++) {
            if (randomItems) {
                itemsPerOrder = ((long) (Math.random() * (itemCount - 1))) + 1;
            } else {
                itemsPerOrder = itemCount;
            }
            orderItems = new ArrayList<OrderItem>();
            order = new Order();
            email = emails.get((int) (Math.random() * emails.size()));
            for (int j = 0; j < itemsPerOrder; j++) {
                productName = productNames.get((int) (Math.random() * productNames.size()));
                orderItem = new OrderItem(productManager.getProductByName(productName), (((long) (Math.random() * (quantity - 1))) + 1)); // don't want zeros!
                orderItems.add(orderItem);
            }
            orderManager.addOrder(email, orderItems);
        }
    }
}
