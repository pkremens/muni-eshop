package cz.fi.muni.eshop.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.basket.BasketManager;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;

import cz.fi.muni.eshop.util.qualifier.TypeResolved;

@Named
@SessionScoped
public class BasketController implements Serializable {
	
	
	@Inject
	@MuniEshopLogger
	private Logger log;

    @Inject
    @TypeResolved
    private BasketManager<ProductEntity> basket;
    @Inject
    @TypeResolved
    private ProductManager productManager;

    @PostConstruct
    public void initNewProduct() {
    	log.warning("init");
        // basket.initNewBasket(); init methot of basket is called in baskets PostConstruct!!!
        
    }
    @Produces
    @Named("basketContent")
    public Collection<ProductEntity> getBasketContent() {
    	log.warning("getBasketContent");
    	return new ArrayList<ProductEntity>(basket.getAllProductsInBasket()); // TODO create local List in BasketBean????
    }
    
    @Produces
    @Named("isBasketEmpty")
    public boolean isBasketEmpty() {
    	log.warning("isEmpty");
    	return basket.isEmpty();
    }
    
    @Produces
    @Named("basketTotalPrice") 
    public Long basketTotalPrice() {
    	log.warning("basketTotalPrice");
        return basket.getTotalPrice();
    }
    
    public void addToBasket(ProductEntity product) {
    	log.warning("addToBasket");
    	basket.addToBasket(product);
    	
    }
    
    public void addMoreToBasket(ProductEntity product, Long quantity) {
    	log.warning("addMoreToBasket");
    	basket.addToBasket(product, quantity);
    	
    }  
    
    public void removeFromBasket(ProductEntity product) {
    	log.warning("removeFromBasket");
    	basket.removeFromBasker(product);
    	
    }
    
    public boolean isInBasket(ProductEntity product) {
    	log.info("Is " + product.getProductName() + " in basket");
    	return basket.isInBasket(product);
    }
    
    public void addMore(ProductEntity product, Long quantity) {
    	log.info("adding to product: " + product + " quantity: " + quantity);
    	basket.productQuantityIncrement(product, quantity);
    }
    
    public void removeMore(ProductEntity product, Long quantity) {
    	log.info("removing product: " + product + " quantity: " + quantity);
    	basket.productQuantityDecrement(product, quantity);
    }
    
    public void clearBasket() {
    	log.warning("clearBasket");
    	basket.clearBasket();
    	
    }
    
    public Long getQuantityOfProduct(ProductEntity product) {
    	log.warning("getQuantityOfProduct");
    	return basket.getQuantityOfProduct(product); 
    }
    
    
    
}
