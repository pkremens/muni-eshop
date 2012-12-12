/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.logging.Logger;
import javax.inject.Inject;
import junit.framework.Assert;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class ProductManagerJPATest {

    @Inject
    Logger log;
    @Inject
    private ProductManager productManager;
    @Inject
    private Product product;

    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "product-test.war").addClasses(Product.class, ProductManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    private void setUp() {
        product = new Product("name", 5L, Category.TYPE1);
        productManager.addProduct(product);
    }

    @After
    public void cleanUp() {
        productManager.clearProductsTable();
    }

    @Test
    public void addProductTest() {
        product = new Product("name", 5L, Category.TYPE1);
        Assert.assertNull(product.getId());
        productManager.addProduct(product);
        Assert.assertNotNull(product.getId());
    }

    @Test
    public void updateTest() {
        setUp();
        long id = product.getId();
        product.setProductName("ASD");
        product = productManager.getProductByName("ASD");
        int hash = productManager.hashCode();
        product = productManager.getProductById(id);
        Assert.assertEquals((long) product.getId(), id);

    }

    public void getAllTest() {
        product = new Product("xxx", 5L, Category.TYPE1);
        productManager.addProduct(product);
        Assert.assertEquals(productManager.getProducts().size(), 2L);
        Assert.assertTrue(productManager.getProducts().contains(product));
    }
}
