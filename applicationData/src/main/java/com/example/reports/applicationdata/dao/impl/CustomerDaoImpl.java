package com.example.reports.applicationdata.dao.impl;

import com.example.reports.applicationdata.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Set;

@Repository
@Transactional
public class CustomerDaoImpl implements GenericDao<Customer, Long>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Customer entity) {
        entityManager.persist(entity);
    }

    @Override
    public Customer findById(Long id) {
        return entityManager.find(Customer.class, id);
    }

    @Override
    public List<Customer> findAll() {
        return entityManager.createQuery(
                        "SELECT DISTINCT c FROM Customer c LEFT JOIN FETCH c.transactions", Customer.class)
                .getResultList();
    }

    @Override
    public void saveAll(Set<Customer> entities) {
            for (Customer entity : entities) {
                entityManager.persist(entity);
            }
    }

    @Override
    public void update(Customer entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(Customer entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}
