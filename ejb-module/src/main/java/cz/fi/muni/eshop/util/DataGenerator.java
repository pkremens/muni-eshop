/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.util;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.ProductManager;
import javax.ejb.Stateless;
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
}
