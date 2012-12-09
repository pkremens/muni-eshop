package cz.fi.muni.eshop.test.basket;

import org.junit.Before;


import cz.fi.muni.eshop.service.basket.BasketBeanMap;

public class BasketBeanMapTest extends AbstractBasketTest {
    @Before
    public void SetUp() {
        basket = new BasketBeanMap();
        basket.initNewBasket();
    }


}