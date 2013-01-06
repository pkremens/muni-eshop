package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Product;
import cz.fi.muni.eshop.model.enums.Category;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    /**
     * Create new product
     * @param name 
     * @param price 
     * @param category
     * @param stored
     * @param reserved
     * @return instance of newly created product
     */
    public Product addProduct(String name, Long price, Category category,
            Long stored, Long reserved) {
        if (getProductByNameCount(name) == 1) {
            log.warning("Product with name=" + name + " is already registered " + Exception.class.getName().toString());
            return null;
        }
        Product product = new Product(name, price, category, stored, reserved);
        log.fine("Adding product: " + product);
        em.persist(product);
        return product;
    }
    
    /**
     * Make this product part of some order = decrease stored, increase reserved
     * @param id
     * @param quantity which will be moved from stored to reserved
     */
    //@TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void orderProduct(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
        log.fine(product.toString() + " on store: " + product.addStored(id));
        if (product.getStored() < product.addReserved(quantity)) {
            product.addStored(1000L);
        }
        em.merge(product);
        }
    
    /**
     * Make this product part of some invoice = decrease reserved
     * @param id 
     * @param quantity to be decreased from reserved
     */
   // @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void invoiceProduct(Long id, Long quantity) {
        Product product = em.find(Product.class, id);
//        if (quantity > product.getReserved()) {
//            throw new IllegalArgumentException(
//                    "Can not invoice non-reserve products, we are somewhere loosing data! product=" + product.toString() + " quantity=" + quantity);
//        }
        product.setStored(product.getStored() - quantity);
        product.setReserved(product.getReserved() - quantity);
        em.merge(product);
    }

    public Product getProductById(Long id) {
        log.fine("Find product by id: " + id);
        return em.find(Product.class, id);
    }

    public Product getProductByName(String name) {
        log.fine("Get product by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Product> getProducts() {
        log.fine("Get all products");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria = cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product);
        return em.createQuery(criteria).getResultList();
    }

    public List<Long> getProductIds() {
        log.fine("Get all products Ids");
        Metamodel mm = em.getMetamodel();
        EntityType<Product> mproduct = mm.entity(Product.class);
        SingularAttribute<Product, Long> id = mproduct.getDeclaredSingularAttribute("id", Long.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product.get(id));
        return em.createQuery(criteria).getResultList();
    }

    public List<String> getProductNames() {
        log.fine("Get all product names");
        Metamodel mm = em.getMetamodel();
        EntityType<Product> mproduct = mm.entity(Product.class);
        SingularAttribute<Product, String> name = mproduct.getDeclaredSingularAttribute("name", String.class);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<String> criteria = cb.createQuery(String.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product.get(name));
        return em.createQuery(criteria).getResultList();
    }

    public Long getProductTableCount() {
        log.fine("Get product table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(cb.count(product));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearProductsTable() {
        log.fine("Clear products table");
        for (Product product : getProducts()) {
            em.remove(product);
        }
    }
    
    
    public Product getRandomProduct() {
        List<Product> products = getProducts();
        Random random = new Random();
        return products.get(random.nextInt(products.size()));
    }

    public void deleteProduct(String name) {
        Product product = getProductByName(name);
        em.remove(product);
    }
    
    /**
     * Get number of products with given name
     * @param name
     * @return 
     */
    public Long getProductByNameCount(String name) {
        log.fine("Get product: " + name + " count in table");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(cb.count(product)).where(cb.equal(product.get("name"), name));
        return em.createQuery(criteria).getSingleResult().longValue();
    }
    
    /**
     * Set reserved=0 for all products in DB
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void unreserveProducts() {
        for (Product product : getProducts()) {
            product.setReserved(0L);
            em.merge(product);
        }
    }
}
