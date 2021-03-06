package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.model.Product;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@SessionScoped
@Named
public class BasketBean implements Serializable{

    private Map<Product, Long> basket;
    private Long totalPrice;

    public void addToBasket(Product product) {
        addToBasket(product, 1L);
    }

    public void addToBasket(Product product, Long quantity) {
        if (basket.containsKey(product)) {
            productQuantityIncrement(product, quantity);
        } else {
            totalPrice += product.getPrice() * quantity;
            basket.put(product, quantity);
        }
    }

    public void productQuantityIncrement(Product product, Long toAdd) {
        totalPrice += product.getPrice() * toAdd;
        long newQuantity = basket.get(product) + toAdd;
        updateInBasket(product, newQuantity);
    }

    public void productQuantityDecrement(Product product, Long toRemove) {
        long maxRemove = (basket.get(product) - 1);
        if (maxRemove == 0) {
            // Can not decrease quantity => ignore
        } else {
            long update = (toRemove > maxRemove) ? maxRemove : toRemove;	// how many will I actually remove
            totalPrice -= product.getPrice() * (update);
            long newQuantity = basket.get(product) - update;
            updateInBasket(product, newQuantity);
        }
    }

    public void updateInBasket(Product product, Long newQuantity) {
        if (newQuantity < 1) {
            basket.put(product, 1L);
        } else {
            basket.put(product, newQuantity);
        }

    }

    public void removeFromBasker(Product product) {
        totalPrice -= product.getPrice() * basket.get(product);
        basket.remove(product);
    }

    public Collection getBasket() {
        return (Collection) basket;
    }

    public boolean isEmpty() {
        return basket.isEmpty();
    }

    @PostConstruct
    public void initNewBasket() {
        totalPrice = 0L;
        basket = new HashMap<Product, Long>();

    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void clearBasket() {
        basket.clear();
        totalPrice = 0L;

    }

    public Collection<Product> getAllProductsInBasket() {
        return basket.keySet();
    }

    public Long getQuantityOfProduct(Product product) {
        if (basket.containsKey(product)) {
            return basket.get(product);
        } else {
            return 0L;
        }

    }

    public boolean isInBasket(Product product) {

        return basket.containsKey(product);
    }
}
