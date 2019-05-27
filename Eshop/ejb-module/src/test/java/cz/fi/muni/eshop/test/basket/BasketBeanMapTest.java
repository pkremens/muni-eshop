package cz.fi.muni.eshop.test.basket;


import org.junit.Before;

public class BasketBeanMapTest extends AbstractBasketTest {

    @Before
    public void SetUp() {
        basket = new BasketBean();
        basket.initNewBasket();
    }
}