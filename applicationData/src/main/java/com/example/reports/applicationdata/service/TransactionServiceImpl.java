package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class TransactionServiceImpl implements GenericService<Transaction, Long> {

    private final GenericDao<Transaction, Long> transactionDao;


    public TransactionServiceImpl(GenericDao<Transaction, Long> transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    public void save(Transaction entity) {
        transactionDao.save(entity);
    }

    @Override
    public Transaction findById(Long id) {
        return transactionDao.findById(id);
    }

    @Override
    public List<Transaction> findAll() {
        return transactionDao.findAll();
    }

    @Override
    public void update(Transaction entity) {
        transactionDao.update(entity);
    }

    @Override
    public void delete(Transaction entity) {
        transactionDao.delete(entity);
    }
}
