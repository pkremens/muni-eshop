/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service.jpa;

import cz.fi.muni.eshop.model.ProductEntity;
import cz.fi.muni.eshop.service.ProductManager;
import cz.fi.muni.eshop.util.quilifier.JPA;
import cz.fi.muni.eshop.util.quilifier.MuniEshopLogger;
import cz.fi.muni.eshop.util.quilifier.MuniEshopDatabase;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

/**
 *
 * @author Petr Kremensky <207855@mail.muni.cz>
 */
@JPA
@Stateless
public class ProductManagerJPA implements ProductManager {
    

    @Inject
    @MuniEshopDatabase
    private EntityManager em;
    @Inject
    @MuniEshopLogger
    private Logger log;

    @Override
    public void addProduct(ProductEntity product) {
        log.log(Level.WARNING, "Add product: {0}", product);
        log.warning("accessing DB");
        em.persist(product);
        log.log(Level.WARNING, "Product added: {0}", product);
        
    }

    @Override
    public void update(ProductEntity product) {
        log.log(Level.WARNING, "Update product: {0}", product);
        log.warning("accessing DB");
        em.merge(product);
    }

    @Override
    public ProductEntity findProductById(long id) throws NoResultException {
        log.log(Level.WARNING, "Find product by id: {0}", id);
        log.warning("accessing DB");
        return em.createNamedQuery("product.findProductById", ProductEntity.class).setParameter("id", id).getSingleResult();

    }

    @Override
    public List<ProductEntity> getProducts() {
        log.warning("Get all products");
        log.warning("accessing DB");
        return em.createNamedQuery("product.getProducts", ProductEntity.class).getResultList();
    }
}
