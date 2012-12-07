/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.ProductEntity;
import java.util.Map;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface BasketManager {
    void addToBasket(ProductEntity product);
    
    void addToBasket(ProductEntity product, Integer quantity);
    
    void productQuantityIncrement(ProductEntity product, Integer toAdd);
    
    void productQuantityDecrement(ProductEntity product, Integer toRemove);

    void updateInBasket(ProductEntity product, Integer newQuantity);

    void removeFromBasker(ProductEntity product);

    Map<ProductEntity, Integer> getBasket();

    boolean isEmpty();

    void initNewBasket();
    
    Long getTotalPrice();
//List<ProductEntity> getAllMessages() throws Exception;
}
