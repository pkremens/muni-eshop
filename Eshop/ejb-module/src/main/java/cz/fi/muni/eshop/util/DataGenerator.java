/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RequestScoped
public class DataGenerator {

    private static final String charset = "0123456789"
            + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    @EJB
    private CustomerManager customerManager;
    @EJB
    private ProductManager productManager;
    @EJB
    private OrderManager orderManager;
    @Inject 
    private Logger log;

    /**
     * Generate given number of customers. Clears customer table before generating to avoid name collisions.
     * @param quantity Number of customers to be generated.
     */
    public void generateCustomers(Long quantity) {
    	log.info("Clearing all customers and generating new, quantity = " + quantity);
    	customerManager.clearCustomersTable();
        for (int i = 0; i < quantity; i++) {
            String base = "customer" + i;
            log.info(customerManager.addCustomer(base + "@mail.xx", base, base).toString());
        }
    }
    
    /**
     * Generate given number of products. Clears product table before generating to avoid name collisions.
     * 
     * @param quantity
     * @param price
     * @param stored
     */
    public void generateProducts(Long quantity, Long price, Long stored) {
    	log.info("Cleating all products and generating new, quantity = " + quantity);
    	productManager.clearProductsTable();
        generateProducts(quantity, price, stored, false);
    }

    public void generateProducts(Long quantity, Long price, Long stored,
            boolean randomStored) {
        for (int i = 0; i < quantity; i++) {
            String base = "product" + i;
            Product product = new Product();
            product.setCategory(generateRandomProductCategory());
            product.setPrice(generateLongOneToN(price));
            if (!randomStored) {
                product.setStored(stored);
            } else {
                product.setStored(generateLongOneToN(stored));
            }
            product.setProductName(base);
            log.info(productManager.addProduct(product.getProductName(),
                    product.getPrice(), product.getCategory(),
                    product.getStored(), product.getReserved()).toString());
        }
    }

    private Long generateLongOneToN(Long n) {
        return ((long) (Math.random() * (n - 1))) + 1;
    }

    /**
     * MUST BE CALLED AFTER PRODUCTS AND CUSTOMERS GENERATION!!!
     *
     * @param quantity number of orders to be generated
     */
    public void generateOrders(Long quantity, Long itemsPerOrder) {
        generateOrders(quantity, itemsPerOrder, false);
    }

    public void generateRandomOrder() {
        long productsCount = productManager.getProductTableCount();
        if (productsCount < 20) {
            generateRandomOrder(productsCount);
        } else {
            generateRandomOrder(20L);
        }

    }

    public void generateRandomOrder(Long itemCount) {
        generateOrders(1L, itemCount, true);
    }

    /**
     * Take all customer emails and store them in list. In for cycle for
     * quantity start creating new orders. In every new order cycle generate
     * random list of products Id (must be unique inside list!) and take random
     * customer as orderer. Do inner loop for itemCount and in every loop create
     * new OrderItem. Use list of random products and use inner loop main
     * variable to go through the list linearly -> use all products in list. Now
     * that we have OrderItems generated we can create new Order using
     * OrderManager.
     *
     * @param quantity how many orders we want to generate
     * @param itemCount how many items will be in each of order
     * @param randomItems whether can number of items differ between orders (max
     * itemCount)
     */
    public void generateOrders(long quantity, long itemCount,
            boolean randomItems) {
        List<String> emails = customerManager.getCustomerEmails();
        if (emails.isEmpty()) {
            throw new NullPointerException("There are customers registered, cannot generate random order. Create some customer first.");
        }
        OrderItem orderItem;
        String email;
        List<OrderItem> orderItems;
        Set<Long> uniqueProductIds;
        long itemsPerOrder;
        for (int i = 0; i < quantity; i++) {
            if (randomItems) {
                itemsPerOrder = ((long) (Math.random() * (itemCount - 1))) + 1;
            } else {
                itemsPerOrder = itemCount;
            }
            uniqueProductIds = randomUniqueProductIds(itemsPerOrder);
            orderItems = new ArrayList<OrderItem>();
            email = emails.get((int) (Math.random() * emails.size()));
            for (Long productId : uniqueProductIds) {
                orderItem = new OrderItem(
                        productManager.getProductById(productId),
                        (generateLongOneToN(20L))); // don't want zeros!
                orderItems.add(orderItem);
            }
            orderManager.addOrder(email, orderItems);
        }
    }

    /**
     * Generate random set of unique products to warrant that we will not try to
     * put same products as different order items into same order.
     *
     * @param itemsPerOrder how many products we want
     * @return List of unique products
     */
    private Set<Long> randomUniqueProductIds(Long itemsPerOrder) {
        Set<Long> uniqueProductIds = new HashSet<Long>(); // to secure uniqueness
        if (itemsPerOrder > productManager.getProductTableCount()) {
            throw new IllegalArgumentException("Cannot generate order with more Order Items than products on store");
        }
        List<Long> productIds = productManager.getProductIds();
        Random randomGenerator = new Random();
        while (uniqueProductIds.size() < itemsPerOrder) {
            uniqueProductIds.add(productIds.remove(randomGenerator.nextInt(productIds.size())));
        }
        return uniqueProductIds;
    }

    public void generateOrder(String customersEmail, List<OrderItem> orderItems) {
        orderManager.addOrder(customersEmail, orderItems);
    }

    public Customer generateRandomCustomer() {
        String base = getRandomString(12);
        int count = 5;
        Customer customer;
        for (int i = 0; i < count; i++) {
            try {
                customer = customerManager.addCustomer(base + "@random.xx",
                        base, base);

                return customer;
            } catch (Exception ex) { // TODO specialize this one 
                // ignore, we hit created customer, try again
            }
        }
        throw new IllegalStateException("Unable to generate customer with unique name!");
    }

    // if we realy don't care
    public Product generateRandomProduct() {
        return generateRandomProduct(generateLongOneToN(1000L), generateLongOneToN(1000L));
    }

    public Product generateRandomProduct(Long price, Long stored) {
        String base = getRandomString(12);
        int count = 5;
        Product product;
        Category category = generateRandomProductCategory();
        for (int i = 0; i < count; i++) {
            try {
                product = productManager.addProduct("pro" + base, price, category, stored, 0L);
                return product;
            } catch (Exception ex) { // TODO specialize this one 
                // ignore, we hit created customer, try again
            }
        }
        throw new IllegalStateException("Unable to product with unique name!");
    }

    private Category generateRandomProductCategory() {
        Category category;
        int type = (int) Math.random() * 7;
        switch (type) {
            case 0:
                category = Category.TYPE1;
                break;
            case 1:
                category = Category.TYPE2;
                break;
            case 2:
                category = Category.TYPE3;
                break;
            case 3:
                category = Category.TYPE4;
                break;
            case 4:
                category = Category.TYPE5;
                break;
            case 5:
                category = Category.TYPE6;
                break;
            case 6:
                category = Category.TYPE7;
                break;
            default:
                throw new IllegalStateException("Debugg me!");
        }
        return category;

    }

    private static String getRandomString(int length) {
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i <= length; i++) {
            int pos = rand.nextInt(charset.length());
            sb.append(charset.charAt(pos));
        }
        return sb.toString();
    }
}