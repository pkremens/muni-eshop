/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.annotation.Resource;
import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.ConnectionFactory;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;
import javax.persistence.metamodel.SingularAttribute;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@Stateless
public class ProductManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;
    private static final int MSG_COUNT = 5; // TODO what is this for???
    @Resource(mappedName = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;
    @Resource(mappedName = "java:/queue/test")
    private Queue queue;

    public Product addProduct(String name, Long price, Category category, Long stored, Long reserved) {
        Product product = new Product(name, price, category, stored, reserved);
        log.info("Adding product: " + product);
        em.persist(product);
        return product;
    }

    public void refillProductWithReserved(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
        log.warning(product.toString());
        product.setStored(product.getReserved() + quantity);
        log.warning("Refilling product: " + product + " new quantity: " + product.getStored());
        em.merge(product);
    }

    public void hardRefillProduct(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
        log.warning(product.toString());
        product.setStored(product.getStored() + quantity);
        log.warning("Refilling product: " + product + " new quantity: " + product.getStored());
        em.merge(product);
    }

    public void orderProduct(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
        product.setReserved(product.getReserved() + quantity); // delat toto tady nebo muzu i v Product
        log.warning(product.toString());
        //log.warning("Updating Product: " + product + " quantity: " + quantity + " and raising stored value to be +100 in compare to reserved");
        // we get into state whe we would not be able to close the order, so storeman must refill the store to getReserver() + 100.
        if (product.getStored() < product.getReserved()) {
            //         noticeStoreman(id);
            product.setStored(product.getReserved() + 100L);
            log.warning("refill: " + product.toString());
        }

        em.merge(product);
    }

    /**
     *
     * @param id
     * @param quantity
     * @return true if product orderItem containing this product can be closed,
     * false if no products on store -> calling storeman
     */
    public void invoiceProduct(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
        log.info("Invoice product: " + product + " quantity: " + quantity);
        if (quantity > product.getReserved()) {
            throw new IllegalArgumentException("Can not invoice non-reserve products, we are somewhere loosing data!");
        }
        if (product.getStored() - quantity < 0) { // pokud bych sel do zaporu na sklade
            throw new NullPointerException("Auto-refill failed!");
        } else {
            product.setStored(product.getStored() - quantity);
            product.setReserved(product.getReserved() - quantity);
            em.merge(product);
        }
    }

    public Product getProductById(Long id) {
        log.info("Find product by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }

    public Product getProductByName(String name) {
        log.info("Get product by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Product> getProducts() {
        log.info("Get all products");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product);
        return em.createQuery(criteria).getResultList();
    }

    public List<Long> getProductIds() {
        log.info("Get all products Ids");
        Metamodel mm = em.getMetamodel();
        EntityType<Product> mproduct = mm.entity(Product.class);
        SingularAttribute<Product, Long> id =
                mproduct.getDeclaredSingularAttribute("id", Long.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product.get(id));
        return em.createQuery(criteria).getResultList();
    }

    public List<String> getProductNames() {
        log.info("Get all product names");
        Metamodel mm = em.getMetamodel();
        EntityType<Product> mproduct = mm.entity(Product.class);
        SingularAttribute<Product, String> name =
                mproduct.getDeclaredSingularAttribute("name", String.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product.get(name));
        return em.createQuery(criteria).getResultList();
    }

    public Long getProductTableCount() {
        log.info("Get product table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(cb.count(product));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearProductsTable() {
        log.info("Clear products table");
        for (Product product : getProducts()) {
            em.remove(product);
        }
    }

    private void noticeStoreman(Long id) {
        Connection connection = null;
        try {
            connection = connectionFactory.createConnection();
            Session session = connection.createSession(false,
                    Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            connection.start();
            MapMessage message = session.createMapMessage();
            message.setStringProperty("type", "FILL_THE_STORE");
            message.setLongProperty("productId", id);
            messageProducer.send(message);
        } catch (JMSException e) {
            log.warning("A problem occurred during the delivery of this message");
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (JMSException e) {
                    log.warning(e.getMessage());
                }
            }
        }
    }
}
