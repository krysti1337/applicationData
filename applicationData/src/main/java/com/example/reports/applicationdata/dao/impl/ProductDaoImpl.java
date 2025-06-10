package com.example.reports.applicationdata.dao.impl;

import com.example.reports.applicationdata.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class ProductDaoImpl implements GenericDao<Product, String> {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Product entity) {
        entityManager.persist(entity);
    }

    @Override
    public Product findById(String id) {
        return entityManager.find(Product.class, id);
    }

    @Override
    public List<Product> findAll() {
        return entityManager.createQuery(
                        "SELECT DISTINCT p FROM Product p LEFT JOIN FETCH p.transactions", Product.class)
                .getResultList();
    }

    @Override
    public void saveAll(Set<Product> entities) {
        for (Product entity : entities) {
            entityManager.persist(entity);
        }
    }

    @Override
    public void update(Product entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(Product entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}
