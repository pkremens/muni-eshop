package cz.fi.muni.eshop.controller;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.eshop.service.BasketManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.SetWithProducts;
import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;

@Named
@SessionScoped
public class BasketController implements Serializable {

    @Inject
    @SetWithProducts
    private BasketManager basket;
    @Inject
    @JPA
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
    
    
    
}
