/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateful
@SessionScoped
@Named
public class BasketBean implements BasketManager {

    @Inject
    @MuniEshopLogger
    private Logger log;
    private Map<ProductEntity, Integer> basket;
    
    
    @Override
    public void addToBasket(ProductEntity product) {
        log.log(Level.INFO, "Add product: {0}", product.toString());
        addToBasket(product, 1);
    }

    @Override
    public void addToBasket(ProductEntity product, Integer quantity) {
          log.log(Level.INFO, "Add product: {0} quantity: {1}", new Object[]{product.toString(), quantity});
        getBasket().put(product, quantity);
    }

    @Override
    public void productQuantityIncrement(ProductEntity product, Integer toAdd) {
        log.log(Level.INFO, "Product quantity increment, product: {0} adding: {1}", new Object[]{product.toString(), toAdd});
        updateInBasket(product, toAdd);
    }

    @Override
    public void productQuantityDecrement(ProductEntity product, Integer toRemove) {
        log.log(Level.INFO, "Product quantity decrement, product: {0} removing: {1}", new Object[]{product.toString(), toRemove});
        int newValue = basket.get(product) - toRemove;
        updateInBasket(product, newValue);
    }

    @Override
    public void updateInBasket(ProductEntity product, Integer newQuantity) {
        if (newQuantity < 1) {
            log.log(Level.INFO, "Update basket - remove product because of negative (0 incl) value - product: {0} new quantity: {1}", new Object[]{product.toString(), newQuantity});
            removeFromBasker(product);
        } else {
            log.log(Level.INFO, "Update basket - updating product: {0} quantity: {1}", new Object[]{product.toString(), newQuantity});
            getBasket().put(product, newQuantity);
        }
    }

    @Override
    public void removeFromBasker(ProductEntity product) {
        log.log(Level.INFO, "Remove from basket: {0}", product.toString());
        getBasket().remove(product);
    }

    @Override
    public Map<ProductEntity, Integer> getBasket() {
        log.info("Get basket");
        return basket;
    }

    @Override
    public boolean isEmpty() {
        log.info("Is basket empty");
        return getBasket().isEmpty();
    }

    @PostConstruct
    @Override
    public void initNewBasket() {
        log.info("Initializing new basket");
        basket = new TreeMap<ProductEntity, Integer>();
    }
}
