package cz.fi.muni.eshop.service.basket;

import java.util.Collection;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.util.qualifier.MapWithProducts;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.SetWithProducts;

@MapWithProducts
@Stateful
@SessionScoped
@Named
public class BasketBeanMap implements BasketManager<ProductEntity> {
	private Map<ProductEntity, Long> basket;
	private Long totalPrice;
	
	@Override
	public void addToBasket(ProductEntity product) {
		addToBasket(product, 1L);
		
	}

	@Override
	public void addToBasket(ProductEntity product, Long quantity) {
		if (basket.containsKey(product)) {
			productQuantityIncrement(product, quantity);
		} else {
		totalPrice += product.getBasePrice() * quantity;
		basket.put(product, quantity);
		}
	}

	@Override
	public void productQuantityIncrement(ProductEntity product, Long toAdd) {
		totalPrice += product.getBasePrice() * toAdd;
		long newQuantity = basket.get(product) + toAdd;
		updateInBasket(product, newQuantity);
		
	}

	@Override
	public void productQuantityDecrement(ProductEntity product, Long toRemove) {
		totalPrice -= product.getBasePrice() * toRemove;
		long newQuantity = basket.get(product) - toRemove;
		updateInBasket(product, newQuantity);
		
	}

	@Override
	public void updateInBasket(ProductEntity product, Long newQuantity) {
		if (newQuantity < 1) {
			removeFromBasker(product);
		} else {
		basket.put(product, newQuantity);
		}
		
		
	}

	@Override
	public void removeFromBasker(ProductEntity product) {
		basket.remove(product);	
	}

	@Override
	public Collection getBasket() {
		return (Collection) basket;
	}

	@Override
	public boolean isEmpty() {
		return basket.isEmpty();
	}

    @PostConstruct
    @Override
	public void initNewBasket() {
		totalPrice = 0L;
		basket = new TreeMap<ProductEntity, Long>();
		
	}

	@Override
	public Long getTotalPrice() {
		return totalPrice;
}
	

	@Override
	public void clearBasket() {
		basket.clear();
		
	}

	@Override
	public Collection<ProductEntity> getAllProductsInBasket() {
		return basket.keySet();
	}

	@Override
	public Long getQuantityOfProduct(ProductEntity product) {
return basket.get(product);
	}

	@Override
	public boolean isInBasket(ProductEntity product) {
		
		return basket.containsKey(product);
	}


}
