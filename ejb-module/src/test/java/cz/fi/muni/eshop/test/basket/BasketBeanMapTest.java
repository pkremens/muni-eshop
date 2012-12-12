package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.service.basket.BasketBean;
import org.junit.Before;

public class BasketBeanMapTest extends AbstractBasketTest {

    @Before
    public void SetUp() {
        basket = new BasketBean();
        basket.initNewBasket();
    }
}