/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.service.basket.BasketBeanList;

import org.jboss.arquillian.junit.Arquillian;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */

public class BasketBeanListTest extends AbstractBasketTest{
    @Before
    public void SetUp() {
        basket = new BasketBeanList();
        basket.initNewBasket();
    }


}
