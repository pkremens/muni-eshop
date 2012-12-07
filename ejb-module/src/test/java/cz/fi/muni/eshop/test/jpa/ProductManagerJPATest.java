/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.CustomerEntity;
import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.service.jpa.ProductManagerJPA;
import cz.fi.muni.eshop.util.Resources;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class ProductManagerJPATest {

    @Inject
    @MyLogger
    Logger log;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "customer.war").addClasses(ProductEntity.class, ProductManager.class, Resources.class,
                ProductManagerJPA.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }
    @Inject
    private ProductEntity productEntity;
    @Inject
    @JPA
    ProductManager productManager;

    @Test
    @InSequence(1)
    public void addProductTest() {
        Assert.assertTrue(productManager.getProducts().isEmpty());
        productEntity = new ProductEntity("Testp1", 20L);
        log.log(Level.INFO, "New Product: {0}", productEntity.toString());
        productManager.addProduct(productEntity);
    }

    @Test
    @InSequence(2)
    public void updateTest() {
        Assert.assertTrue(productEntity.getId() == null);
        productEntity = productManager.findProductById(1L);
        Assert.assertEquals("Testp1", productEntity.getProductName());
        log.info(productEntity.toString());
        productEntity.setProductName("TestName");
        productManager.update(productEntity);
        log.info(productEntity.toString());

    }

    @Test
    @InSequence(3)
    public void getAllTest() {
        Assert.assertFalse(productManager.getProducts().isEmpty());
    }
}