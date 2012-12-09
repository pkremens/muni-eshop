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
        empty = true;
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
    
    public void addToBasket(ProductEntity product, String value) {
    	System.out.println("product = " + product);
    	System.out.println("value = " + value);
    	basket.addToBasket(product, Long.parseLong(value));
    	empty = false;
    }  
    
    public boolean isInBasket(ProductEntity product) {
    	log.info("Is " + product.getProductName() + " in basket");
    	return basket.isInBasket(product);
    }
    
}
