package cz.fi.muni.eshop.controller;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Identity;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.OrderEntity;
import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.OrderManager;
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
    
    @Inject
    @TypeResolved
    private OrderManager orderManager;
    
    @Inject
    private Identity identity;
    
    @Inject
    private OrderEntity order;
    
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
    
    public void makeOrder() {
    	log.warning("Making order");
    	if (basket.isEmpty()) {
    		throw new NullPointerException("Nothind to order"); // TODO change exceptions later.. return bool maybe
    	}
    	if (!identity.isLoggedIn()) {
    		throw new IllegalStateException("User must be logged in if he wants to make order"); // TODO change exceptions later
    	}
    	order.setCustomer((CustomerEntity) identity.getUser());
    	List<OrderLineEntity> lines = new ArrayList<OrderLineEntity>();
    	for (ProductEntity product : basket.getAllProductsInBasket()) {
			lines.add(new OrderLineEntity(productManager.findProductById(product.getId()), basket.getQuantityOfProduct(product)));
		}
    	order.setOrderLines(lines);
    	orderManager.addOrder(order);
    	basket.clearBasket();
    	order = new OrderEntity(); // TODO is needed? trace logs 
    }
}
