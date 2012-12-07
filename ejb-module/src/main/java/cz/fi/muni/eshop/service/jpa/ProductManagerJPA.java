/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MyLogger;
import cz.fi.muni.eshop.util.quilifier.UserDatabase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class ProductManagerJPA implements ProductManager {
    

    @Inject
    @UserDatabase
    private EntityManager em;
    @Inject
    @MyLogger
    private Logger log;

    @Override
    public void addProduct(ProductEntity product) {
        log.log(Level.INFO, "Add product: {0}", product);
        em.persist(product);
        log.log(Level.INFO, "Product added: {0}", product);
        
    }

    @Override
    public void update(ProductEntity product) {
        log.log(Level.INFO, "Update product: {0}", product);
        em.merge(product);
    }

    @Override
    public ProductEntity findProductById(long id) {
        log.log(Level.INFO, "Find product by id: {0}", id);
        return em.createNamedQuery("product.findProductById", ProductEntity.class).setParameter("id", id).getSingleResult();

    }

    @Override
    public List<ProductEntity> getProducts() {
        log.info("Get all products");
        return em.createNamedQuery("product.getProducts", ProductEntity.class).getResultList();
    }
}
