/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.basket;


import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Test;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
public abstract class AbstractBasketTest {

    BasketBean basket;

    @After
    public void clearBasket() {
        basket.clearBasket();
    }

    @Test
    public void addToBasketTest() {
        Product product = new Product("last", 8L, Category.TYPE1);
        product.setId(15L);
        basket.addToBasket(product);
        for (long i = 5; i < 10; i++) {
            product = new Product("name" + i, i,Category.TYPE2);
            product.setId(i);
            basket.addToBasket(product);
        }
        product = new Product("first", 78L, Category.TYPE3);
        product.setId(1L);
        basket.addToBasket(product);
        Assert.assertFalse(basket.isEmpty());
        basket.clearBasket();
        Assert.assertEquals((Long) 0L, basket.getTotalPrice());
        Assert.assertTrue(basket.isEmpty());
    }

    @Test
    public void updateTest() {
        Product product = new Product("Name", 77L, Category.TYPE4);
        product.setId(1L);
        Assert.assertFalse(basket.isInBasket(product));
        basket.addToBasket(product);
        Assert.assertFalse(basket.isEmpty());
        Assert.assertEquals((Long) (77L * 1L), basket.getTotalPrice());
        basket.productQuantityDecrement(product, 1L);
        Assert.assertFalse(basket.isEmpty()); // QuantityDecrement should not remove product from basket, min value = 1;
        Assert.assertEquals((Long) (77L * 1L), basket.getTotalPrice());
        Assert.assertEquals((Long) 1L, basket.getQuantityOfProduct(product));
        basket.addToBasket(product);
        Assert.assertEquals((Long) 2L, basket.getQuantityOfProduct(product));
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
        Product product = new Product("Name", 77L, Category.TYPE6);
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
