/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Model;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Model
public class ProductController {
    @Inject
    private Logger log;
    @Inject 
    private ProductManager productManager;
    @Inject
    private Product newProduct;
    private List<Product> productList;
    
    @PostConstruct
    public void init() {
        
        initNewProduct();
    }
    private void initNewProduct() {
        newProduct = new Product();
    }
    
    public void createNewProduct() {
        productManager.addProduct(newProduct.getProductName(), newProduct.getPrice(), newProduct.getCategory(), newProduct.getStored(), 0L);
        initNewProduct();
    }
    public List<Product> getProducts() {
        return productManager.getProducts();
    }
    
    public void refillProduct(Long id, long quantity) {
            productManager.refillProduct(id, quantity);
    }
}
