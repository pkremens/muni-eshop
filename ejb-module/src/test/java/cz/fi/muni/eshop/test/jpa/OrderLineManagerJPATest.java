/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.test.jpa;

import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.service.ProductManager;
import java.util.logging.Logger;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;
import org.jboss.arquillian.junit.Arquillian;
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
    Logger log;
    @Inject
    OrderItem orderLine;
    @Inject
    Product product;
    @Inject
    ProductManager productManager;
    @Inject
    EntityManager em;
    @Inject
    UserTransaction utx;

    /*
     * TRY @PersistenceContext(unitName = "jeelabPU") @Produces @Default
     * EntityManager em;
     */
//    @Deployment
//    public static Archive<?> createTestArchive() {
//        return ShrinkWrap.create(WebArchive.class, "orderLine.war").addClasses(Product.class, Customer.class, Order.class, ProductManagerJPA.class, ProductManager.class, OrderItem.class, OrderLineManager.class,
//                OrderLineManagerJPA.class, MuniEshopDatabase.class, Role.class, IdentityType.class, User.class, JpaTestResources.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml") // Deploy our test datasource
//                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
//    }
//
//    @Before
//    public void setUp() throws Exception {
//        product.setBasePrice(44L);
//        product.setProductName("Product");
//        product.setEditable(false);
//        product.setOnStore(132L);
//        utx.begin();
//        em.joinTransaction();
//        em.persist(product);
//        orderLine = new OrderItem(product, 6L);
//        em.persist(orderLine);
//        utx.commit();
//    }
//
//    @Test
//    public void orderLineTest() {
//        orderLine = orderLineManager.getOrderLines().get(0);
//        Assert.assertEquals((Long) 44L, orderLine.getProduct().getBasePrice());
//        Assert.assertEquals("Product", orderLine.getProduct().getProductName());
//        Assert.assertEquals((Long) 6L, orderLine.getQuantity());
//    }
    @Test
    @Ignore
    public void test() {
    }
}
