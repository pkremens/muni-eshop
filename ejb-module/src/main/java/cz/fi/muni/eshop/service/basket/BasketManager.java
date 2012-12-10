/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.basket;

import java.util.Collection;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public interface BasketManager<T> {

    void addToBasket(T product);

    void addToBasket(T product, Long quantity);

    void productQuantityIncrement(T product, Long toAdd);

    /**
     * This method can be used ONLY for decreasing quantity of product in
     * basket. Can not be used to remove it as quantity is set always to 1 when
     * trying this.
     *
     * @param product to be updated
     * @param toRemove to be removed
     */
    void productQuantityDecrement(T product, Long toRemove);

    void updateInBasket(T product, Long newQuantity);

    void removeFromBasker(T product);

    Collection getBasket(); // TODO is there better superinterface for List and Map?

    boolean isEmpty();

    void initNewBasket();

    Long getTotalPrice();

    void clearBasket();

    Collection<T> getAllProductsInBasket();

    Long getQuantityOfProduct(T product);

    boolean isInBasket(T product);
//List<ProductEntity> getAllMessages() throws Exception;
}
