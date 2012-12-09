package cz.fi.muni.eshop.controller;

import java.io.Serializable;
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
import cz.fi.muni.eshop.util.qualifier.SetWithProducts;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;

@Named
@SessionScoped
public class BasketController implements Serializable {
	private boolean empty;
	
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
        empty = basket.isEmpty();
    }
    
    @Produces
    @Named("isBasketEmpty")
    public boolean isBasketEmpty() {
    	return empty;
    }
    
    @Produces
    @Named("basketTotalPrice") 
    public Long basketTotalPrice() {
        return basket.getTotalPrice();
    }
    
    public void addToBasket(ProductEntity product) {
    	basket.addToBasket(product);
    	empty = basket.isEmpty();
    }
    
    public void addMoreToBasket(ProductEntity product, String value) {
    	System.out.println("product = " + product);
    	System.out.println("value = " + value);
    	basket.addToBasket(product, Long.parseLong(value));
    	
    }  
    
    public void removeFromBasket(ProductEntity product) {
    	basket.removeFromBasker(product);
    	empty = basket.isEmpty();
    }
    
    public boolean isInBasket(ProductEntity product) {
    	log.info("Is " + product.getProductName() + " in basket");
    	return basket.isInBasket(product);
    }
    
    public void addMore(ProductEntity product, Long quantity) {
    	log.info("adding to product: " + product + " quantity: " + quantity);
    	basket.addToBasket(product, quantity);
    }
    
    public void clearBasket() {
    	basket.clearBasket();
    	empty=basket.isEmpty();
    }
    
    public Long getQuantityOfProduct(ProductEntity product) {
    	return basket.getQuantityOfProduct(product); 
    }
    
}
