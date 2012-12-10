package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.service.basket.BasketBeanMap;
import org.junit.Before;

public class BasketBeanMapTest extends AbstractBasketTest {

    @Before
    public void SetUp() {
        basket = new BasketBeanMap();
        basket.initNewBasket();
    }
}