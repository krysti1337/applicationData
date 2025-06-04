package com.example.reports.applicationdata.dao.impl;


import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TransactionDaoImpl implements GenericDao<Transaction, Long>{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void save(Transaction entity) {
        entityManager.persist(entity);
    }

    @Override
    public Transaction findById(Long id) {
        return entityManager.find(Transaction.class, id);
    }

    @Override
    public List<Transaction> findAll() {
        return entityManager.createQuery(
                        "SELECT t FROM Transaction t " +
                                "JOIN FETCH t.customer " +
                                "JOIN FETCH t.product", Transaction.class)
                .getResultList();
    }

    @Override
    public void update(Transaction entity) {
        entityManager.merge(entity);
    }

    @Override
    public void delete(Transaction entity) {
        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
    }
}
