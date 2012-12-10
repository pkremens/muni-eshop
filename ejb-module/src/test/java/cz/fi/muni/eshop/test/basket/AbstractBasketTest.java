/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.basket.BasketManager;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public abstract class AbstractBasketTest {

    BasketManager<ProductEntity> basket;

    @After
    public void clearBasket() {
        basket.clearBasket();
    }

    @Test
    public void addToBasketTest() {
        ProductEntity product = new ProductEntity("last", 8L);
        product.setId(15L);
        basket.addToBasket(product);
        for (long i = 5; i < 10; i++) {
            product = new ProductEntity("name" + i, i);
            product.setId(i);
            basket.addToBasket(product);
        }
        product = new ProductEntity("first", 78L);
        product.setId(1L);
        basket.addToBasket(product);
        for (ProductEntity productX : basket.getAllProductsInBasket()) {
            System.out.println(productX);
        }

        Assert.assertFalse(basket.isEmpty());

        basket.clearBasket();
        Assert.assertEquals((Long) 0L, basket.getTotalPrice());
        Assert.assertTrue(basket.isEmpty());
    }

    @Test
    public void updateTest() {
        ProductEntity product = new ProductEntity("Name", 77L);
        product.setId(1L);
        Assert.assertFalse(basket.isInBasket(product));
        basket.addToBasket(product);
        Assert.assertFalse(basket.isEmpty());
        basket.productQuantityDecrement(product, 1L);
        Assert.assertFalse(basket.isEmpty()); // QuantityDecrement should not remove product from basket, min value = 1;
        Assert.assertEquals((Long) (77L * 1L), basket.getTotalPrice());
        basket.addToBasket(product);
        Assert.assertEquals((Long) 1L, basket.getQuantityOfProduct(product));
        basket.clearBasket();
        Assert.assertTrue(basket.isEmpty());
        basket.addToBasket(product, 7L);
        Assert.assertEquals((Long) 7L, basket.getQuantityOfProduct(product));
        basket.removeFromBasker(product);
        Assert.assertTrue(basket.isEmpty());
    }

    @Test
    public void increaseAndDecreaseTest() {
        Assert.assertTrue(basket.isEmpty());
        ProductEntity product = new ProductEntity("Name", 77L);
        product.setId(1L);
        basket.addToBasket(product);
        basket.productQuantityIncrement(product, 8L);
        Assert.assertEquals((Long) 9L, basket.getQuantityOfProduct(product));
        Assert.assertEquals((Long) (77L * 9L), basket.getTotalPrice());
        basket.productQuantityDecrement(product, 5L);
        Assert.assertEquals((Long) 4L, basket.getQuantityOfProduct(product));
        Assert.assertEquals((Long) (77L * 4L), basket.getTotalPrice());
        basket.productQuantityDecrement(product, 4L);
        Assert.assertFalse(basket.isEmpty());
        basket.clearBasket();
        Assert.assertTrue(basket.isEmpty());
    }
}
