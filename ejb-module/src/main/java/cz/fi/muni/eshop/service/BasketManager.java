/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.OrderLineEntity;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.PostConstruct;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface BasketManager<T> {
    void addToBasket(T product);
    
    void addToBasket(T product, Long quantity);
    
    void productQuantityIncrement(T product, Long toAdd);
    
    void productQuantityDecrement(T product, Long toRemove);

    void updateInBasket(T product, Long newQuantity);

    void removeFromBasker(T product);

    Collection getBasket(); // TODO is there better superinterface for List and Map?

    boolean isEmpty();
    
    @PostConstruct
    void initNewBasket();
    
    Long getTotalPrice();
    
    void clearBasket();
    
    Collection<T> getAllProductsInBasket(); 
    
    Long getQuantityOfProduct(T product);
//List<ProductEntity> getAllMessages() throws Exception;
}
