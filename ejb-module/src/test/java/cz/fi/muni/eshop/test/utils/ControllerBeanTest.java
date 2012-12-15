/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.fi.muni.eshop.test.utils;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Invoice;
import cz.fi.muni.eshop.model.InvoiceItem;
import cz.fi.muni.eshop.model.Order;
import cz.fi.muni.eshop.model.OrderItem;
import cz.fi.muni.eshop.model.OrderRoot;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import cz.fi.muni.eshop.service.CustomerManager;
import cz.fi.muni.eshop.service.OrderManager;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.test.DummyMDB;
import cz.fi.muni.eshop.test.TestResources;
import cz.fi.muni.eshop.util.ControllerBean;
import cz.fi.muni.eshop.util.DataGenerator;
import javax.inject.Inject;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@RunWith(Arquillian.class)
public class ControllerBeanTest {
//TODO test zkusim udelat nejakou dummy controll bean, ktera bude mazat kazdou sekundu db, neco hodim do db pockam 1100ms Assert prazdnu neco hodim, pockam sekundu a zase assert
    @Inject
    private ControllerBean controller;
    
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "generator-test.war").addClasses(ControllerBean.class, DummyMDB.class, OrderRoot.class, OrderManager.class, ProductManager.class, DataGenerator.class, OrderItem.class, Product.class, InvoiceItem.class, Invoice.class, Order.class, Customer.class, TestResources.class, Category.class, CustomerManager.class).addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml").addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }
        
        @Before
        public void cleanBefore() {
            controller.clearDB();
        }
        @Test
        public void clearDbTest() {
            
        }
    

    
    
}
