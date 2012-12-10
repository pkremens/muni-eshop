/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.basket;

import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.util.qualifier.ListWithProducts;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
@ListWithProducts
@Stateful
@SessionScoped
@Named
@Deprecated // not tested outside testCase, use BasketBeanMap instead 
public class BasketBeanList implements BasketManager<ProductEntity> {

    @Inject
    @MuniEshopLogger
    private Logger log;
    private List<OrderLineEntity> basket;

    @Override
    public void addToBasket(ProductEntity product) {
        basket.add(new OrderLineEntity(product, 1L));
    }

    @Override
    public void addToBasket(ProductEntity product, Long quantity) {

        if (getQuantityOfProduct(product) > 0) {
            productQuantityIncrement(product, quantity);
        } else {
            basket.add(new OrderLineEntity(product, quantity));
        }
    }

    @Override
    public void productQuantityIncrement(ProductEntity product, Long toAdd) {
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                orderLineEntity.addQuantity(toAdd);
                return;
            }
        }
    }

    @Override
    public void productQuantityDecrement(ProductEntity product, Long toRemove) {
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                long newQuantity = (orderLineEntity.getQuantity() - toRemove);
                if (newQuantity <= 0) {
                    orderLineEntity.setQuantity(1L);
                } else {
                    orderLineEntity.setQuantity(newQuantity);
                }
                return;
            }
        }
    }

    @Override
    public void updateInBasket(ProductEntity product, Long newQuantity) {
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                orderLineEntity.setQuantity(newQuantity);
                return; // TODO opravdu se ty zmeny projevi i v kolekci? meli by.. TEST
            }
        }
    }

    @Override // ugly
    public void removeFromBasker(ProductEntity product) {
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                basket.remove(orderLineEntity);
                return;
            }
        }
    }

    @Override
    public List<OrderLineEntity> getBasket() {
        return basket;
    }

    @Override
    public boolean isEmpty() {
        return basket.isEmpty();
    }

    @Override
    @PostConstruct
    public void initNewBasket() {
        basket = new ArrayList<OrderLineEntity>();
    }

    @Override
    public Long getTotalPrice() {
        Long totalPrice = 0L;
        for (OrderLineEntity orderLine : basket) {
            totalPrice += orderLine.getPrice();
        }
        return totalPrice;
    }

    @Override
    public void clearBasket() {
        basket.clear();
    }

    @Override
    public Collection<ProductEntity> getAllProductsInBasket() {
        Set<ProductEntity> products = new TreeSet<ProductEntity>();
        for (OrderLineEntity orderLineEntity : basket) {
            products.add(orderLineEntity.getProduct());
        }
        return products;
    }

    @Override
    public Long getQuantityOfProduct(ProductEntity product) {
        Long quantity = 0L;
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                quantity = orderLineEntity.getQuantity();
                break;
            }

        }
        return quantity;
    }

    @Override
    public boolean isInBasket(ProductEntity product) {
        for (OrderLineEntity orderLineEntity : basket) {
            if (orderLineEntity.getProduct().equals(product)) {
                return true;
            }

        }
        return false;
    }
}
