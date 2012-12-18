package cz.fi.muni.eshop.controller;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.util.Identity;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

@SessionScoped
@Named
public class BasketBean implements Serializable {

    @Inject
    private Logger log;
    @EJB
    private OrderManager orderManager;
    @Inject
    private Identity identity;
    private Set<Long> ids;
    private Map<Product, Long> basket;
    private Long totalPrice;

    public void initNewBasket() {
        totalPrice = 0L;
        basket = new HashMap<Product, Long>();
        ids = new HashSet<Long>();
    }

    public void makeOrder() {
        if (isEmpty()) {
            return;
        }
        Map<Long, Long> productWithQuantity = new HashMap<Long, Long>();
        for (Product product : basket.keySet()) {
            productWithQuantity.put(product.getId(), basket.get(product));
        }
        // sending just email and product ids with quantity
        orderManager.addOrderWithMap(identity.getCustomer().getEmail(),
                productWithQuantity);
        clearBasket();
    }

    // create new Object, now we can dummy products on basket page, but otherwise use only product id as eg. product reserved could change in the time we are making order
    public void addToBasket(Product product) {
        Product addProduct = new Product(product.getProductName(), product.getPrice(), product.getCategory(), product.getStored(), product.getReserved());
        addProduct.setId(product.getId());
        addToBasket(addProduct, 1L);
    }

    public void addToBasket(Product product, Long quantity) {
        log.warning("adding to basket: " + product.toString());
        if (basket.containsKey(product)) {
            productQuantityIncrement(product, quantity);
        } else {
            totalPrice += product.getPrice() * quantity;
            ids.add(product.getId());
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
            long update = (toRemove > maxRemove) ? maxRemove : toRemove;
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
        ids.remove(product.getId());
    }

    public List<Product> getBasket() {
        return new ArrayList<Product>(basket.keySet());
    }

    public boolean isEmpty() {
        return ids.isEmpty();
    }

    public Long getTotalPrice() {
        return totalPrice;
    }

    public void clearBasket() {
        basket.clear();
        totalPrice = 0L;
        ids.clear();
    }

    public Long getQuantityOfProduct(Product product) {
        if (basket.containsKey(product)) {
            return basket.get(product);
        } else {
            return 0L;
        }
    }

    public boolean isInBasket(Long id) {
        log.warning("Is in basket? id=" + id + " result=" + ids.contains(id));
        log.warning(ids.toString());
        return ids.contains(id);
    }
}
