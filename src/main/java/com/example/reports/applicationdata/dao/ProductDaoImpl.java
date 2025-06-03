package com.example.reports.applicationdata.dao;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.List;

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
        return entityManager.createQuery("from Product", Product.class).getResultList();
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
