/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Customer;
import cz.fi.muni.eshop.model.Product;
import java.util.List;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

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
    
    public void addProduct(Product product) {
        log.info("Adding product: " + product);
        em.persist(product);
    }
    
    public void updateProduct(Product product) {
        log.info("Updating product: " + product);
        em.merge(product);
    }
    
    public Product getProductById(Long id) {
        log.info("Find product by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria =  cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public Product getProductByName(String name) {
        log.info("Get product by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria =  cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product).where(cb.equal(product.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }
    
    public List<Product> getProducts() {
        log.info("Get all products");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Product> criteria =  cb.createQuery(Product.class);
        Root<Product> product = criteria.from(Product.class);
        criteria.select(product);
        return em.createQuery(criteria).getResultList();
    }
    
        public void clearProductsTable() {
        List<Product> products = getProducts();
        em.getTransaction().begin();
        for (Product product : products) {
            em.remove(product);
        }
        em.getTransaction().commit();
    } 
}
