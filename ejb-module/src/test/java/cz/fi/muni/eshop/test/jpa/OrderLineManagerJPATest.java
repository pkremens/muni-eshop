/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.OrderLineEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.OrderLineManager;
import cz.fi.muni.eshop.service.jpa.OrderLineManagerJPA;
import cz.fi.muni.eshop.util.Resources;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import cz.fi.muni.eshop.util.quilifier.UserDatabase;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
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
    @MyLogger
    Logger log;
    @Inject
    @JPA
    OrderLineManager manager;
    @Inject
    OrderLineEntity orderLine;
    @Inject
    ProductEntity product;
    @Inject
    @UserDatabase
    EntityManager em;

    /*
     * TRY 
     * @PersistenceContext(unitName = "jeelabPU") @Produces @Default
     * EntityManager em;
     */
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "orderLine.war").addClasses(ProductEntity.class, OrderLineEntity.class, OrderLineManager.class, Resources.class,
                OrderLineManagerJPA.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Test
    @InSequence(1)
    @Ignore("Need Product manager first")
    public void addOrderLineTest() {
    }

    @Test
    @InSequence(2)
    @Ignore("Need Product manager first")
    public void getOrderlinesTest() {
        Assert.assertFalse(manager.getOrderLines().isEmpty());
    }
}
