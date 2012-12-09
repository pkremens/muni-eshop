/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.test.basket;

import org.junit.Before;

import cz.fi.muni.eshop.service.basket.BasketBeanSet;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */

public class BasketBeanSetTest extends AbstractBasketTest{

    
    @Before
    public void SetUp() {
        basket = new BasketBeanSet();
        basket.initNewBasket();
    }


}