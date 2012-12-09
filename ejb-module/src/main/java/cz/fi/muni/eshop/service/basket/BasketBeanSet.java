/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.basket;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.qualifier.SetWithProducts;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@SetWithProducts
@Stateful
@SessionScoped
@Named
@Deprecated // not tested outside testCase, use BasketBeanMap instead 
public class BasketBeanSet implements BasketManager<ProductEntity> {

    //TODO Logs are breaking Arquillian test because don't using container.
   // @Inject
  //  @MuniEshopLogger
  //  private Logger log;
    private SortedSet<ProductEntity> basket;

    @Override
    public void addToBasket(ProductEntity product) {
    //    log.log(Level.INFO, "Add product: {0}", product.toString());
        addToBasket(product, 1L);
    }

    @Override
    public void addToBasket(ProductEntity product, Long quantity) {
    //    log.log(Level.INFO, "Add product: {0} quantity: {1}", new Object[]{product.toString(), quantity});
    	ProductEntity productToAdd = new ProductEntity(product.getProductName(), product.getBasePrice());
    	productToAdd.setId(product.getId());
        product.setQuantityInBasket(quantity);
        basket.add(product);
    }

    @Override
    public void productQuantityIncrement(ProductEntity product, Long toAdd) {
     //   log.log(Level.INFO, "Product quantity increment, product: {0} adding: {1}", new Object[]{product.toString(), toAdd});
        updateInBasket(product, (toAdd + product.getQuantityInBasket()));
    }

    @Override
    public void productQuantityDecrement(ProductEntity product, Long toRemove) {
   //     log.log(Level.INFO, "Product quantity decrement, product: {0} removing: {1}", new Object[]{product.toString(), toRemove});
        long newValue = product.getQuantityInBasket() - toRemove;
        updateInBasket(product, newValue);
    }

    @Override
    public void updateInBasket(ProductEntity product, Long newQuantity) {
        if (newQuantity < 1) {
           // log.log(Level.INFO, "Update basket - remove product because of negative (0 incl) value - product: {0} new quantity: {1}", new Object[]{product.toString(), newQuantity});
            removeFromBasker(product);
        } else {
          //  log.log(Level.INFO, "Update basket - updating product: {0} quantity: {1}", new Object[]{product.toString(), newQuantity});
            product.setQuantityInBasket(newQuantity);
        }
    }

    @Override
    public void removeFromBasker(ProductEntity product) {
    //    log.log(Level.INFO, "Remove from basket: {0}", product.toString());
        basket.remove(product);
    }

    @Override
    public Set<ProductEntity> getBasket() {
     //   log.info("Get basket");
        return basket;
    }

    @Override
    public boolean isEmpty() {
      //  log.info("Is basket empty");
        return basket.isEmpty();
    }

    // TODO TRY WITH POSTCONSTRUCT IN INTERFACE
    @Override
    public void initNewBasket() {
       // log.info("Initializing new basket");
        basket = new TreeSet<ProductEntity>();
    }

    // TODO
    @Override
    public Long getTotalPrice() {
        long totalPrice = 0L;
        for (ProductEntity product : basket) {
            totalPrice += (product.getBasePrice() * product.getQuantityInBasket());
        }
        return totalPrice;
    }
    
    @Override
    public void clearBasket() {
        basket.clear();
    }

    
    @Override // redundant in this implementation of BasketManager
    public Collection<ProductEntity> getAllProductsInBasket() {
        return basket;
    }

    @Override
    public Long getQuantityOfProduct(ProductEntity product) {
        return product.getQuantityInBasket();
    }
}
