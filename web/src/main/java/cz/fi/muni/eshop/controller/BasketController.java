package cz.fi.muni.eshop.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.basket.BasketManager;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.SetWithProducts;
import cz.fi.muni.eshop.util.qualifier.TypeResolved;

@Named
@SessionScoped
public class BasketController implements Serializable {
	
	@Inject
	@MuniEshopLogger
	private Logger log;

    @Inject
    @TypeResolved
    private BasketManager basket;
    @Inject
    @TypeResolved
    private ProductManager productManager;

    @PostConstruct
    public void initNewProduct() {
        basket.initNewBasket();
    }
    
    @Produces
    @Named("isBasketEmpty")
    public boolean isBasketEmpty() {
    	System.out.println(basket.isEmpty());
        return basket.isEmpty();
    }
    
    @Produces
    @Named("basketTotalPrice") 
    public Long basketTotalPrice() {
        return basket.getTotalPrice();
    }
    
    public void addToBasket() {
    	log.info("Add to basket");
    }
    
    
    
}
