/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.OrderLineManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.jpa.OrderLineManagerJPA;
import cz.fi.muni.eshop.service.jpa.ProductManagerJPA;
import cz.fi.muni.eshop.util.Resources;
import cz.fi.muni.eshop.util.qualifier.JPA;
import cz.fi.muni.eshop.util.qualifier.MuniEshopDatabase;
import cz.fi.muni.eshop.util.qualifier.MuniEshopLogger;

import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class OrderLineManagerJPATest {

    @Inject
    @MuniEshopLogger
    Logger log;
    @Inject
    @JPA
    OrderLineManager orderLineManager;
    @Inject
    OrderLineEntity orderLine;
    @Inject
    ProductEntity product;
    @Inject
    @JPA
    ProductManager productManager;
    
    @Inject
    @MuniEshopDatabase
    EntityManager em;
    @Inject
    UserTransaction utx;

    /*
     * TRY @PersistenceContext(unitName = "jeelabPU") @Produces @Default
     * EntityManager em;
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "orderLine.war").addClasses(ProductEntity.class, ProductManagerJPA.class, ProductManager.class, OrderLineEntity.class, OrderLineManager.class, 
                OrderLineManagerJPA.class, MuniEshopDatabase.class, JpaTestResources.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Before
    public void setUp() throws Exception {
        product.setBasePrice(44L);
        product.setProductName("Product");
        product.setEditable(false);
        utx.begin();
        em.joinTransaction();
        em.persist(product);
        orderLine = new OrderLineEntity(product, 6L);
        em.persist(orderLine);
        utx.commit();
    }
    @Test
    public void orderLineTest() {
        orderLine = orderLineManager.getOrderLines().get(0);
        Assert.assertEquals((Long) 44L, orderLine.getProduct().getBasePrice());
        Assert.assertEquals("Product", orderLine.getProduct().getProductName());
        Assert.assertEquals((Long) 6L, orderLine.getQuantity());
    }
}
