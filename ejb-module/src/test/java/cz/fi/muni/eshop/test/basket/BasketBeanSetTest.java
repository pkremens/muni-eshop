/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.test.basket;

import cz.fi.muni.eshop.data.member.MemberListProducer;
import cz.fi.muni.eshop.data.member.MemberRepository;
import cz.fi.muni.eshop.model.Member;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.BasketBeanSet;
import cz.fi.muni.eshop.service.BasketManager;
import cz.fi.muni.eshop.service.MemberRegistration;
import cz.fi.muni.eshop.testpackage.Dummy;
import cz.fi.muni.eshop.util.Resources;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */

public class BasketBeanSetTest extends AbstractBasketTest{
//        @Deployment
//    public static Archive<?> createTestArchive() {
//        return ShrinkWrap.create(WebArchive.class, "test.war").addClasses(BasketBeanSet.class, AbstractBasketTest.class, MuniEshopLogger.class, BasketManager.class, Resources.class, ProductEntity.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
//                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
//    }
    
    @Before
    public void SetUp() {
        basket = new BasketBeanSet();
        basket.initNewBasket();
    }


}