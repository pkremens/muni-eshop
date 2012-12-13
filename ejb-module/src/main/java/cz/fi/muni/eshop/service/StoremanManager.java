/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.eshop.service;

import cz.fi.muni.eshop.model.Storeman;
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
public class StoremanManager {

    @Inject
    private EntityManager em;
    @Inject
    private Logger log;

    public void addProduct(Storeman storeman) {
        log.info("Adding storeman: " + storeman);
        em.persist(storeman);
    }

    public void updateStoreman(Storeman storeman) {
        log.info("Updating product: " + storeman);
        em.merge(storeman);
    }

    public Storeman getStoremanById(Long id) {
        log.info("Find storeman by id: " + id);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Storeman> criteria = cb.createQuery(Storeman.class);
        Root<Storeman> storeman = criteria.from(Storeman.class);
        criteria.select(storeman).where(cb.equal(storeman.get("id"), id));
        return em.createQuery(criteria).getSingleResult();
    }

    public Storeman getStoremanByName(String name) {
        log.info("Get storeman by name: " + name);
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Storeman> criteria = cb.createQuery(Storeman.class);
        Root<Storeman> storeman = criteria.from(Storeman.class);
        criteria.select(storeman).where(cb.equal(storeman.get("name"), name));
        return em.createQuery(criteria).getSingleResult();
    }

    public List<Storeman> getStoremen() {
        log.info("Get all storemen");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Storeman> criteria = cb.createQuery(Storeman.class);
        Root<Storeman> storeman = criteria.from(Storeman.class);
        criteria.select(storeman);
        return em.createQuery(criteria).getResultList();
    }

    public Long getStoremanTableCount() {
        log.info("Get storeman table status");
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Long> criteria = cb.createQuery(Long.class);
        Root<Storeman> storeman = criteria.from(Storeman.class);
        criteria.select(cb.count(storeman));
        return em.createQuery(criteria).getSingleResult().longValue();
    }

    public void clearStoremanTable() {
        log.info("Clear storeman table");
        for (Storeman storeman : getStoremen()) {
            em.remove(storeman);
        }
    }
}
