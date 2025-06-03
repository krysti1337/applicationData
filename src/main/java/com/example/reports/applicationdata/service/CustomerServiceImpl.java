package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Customer;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CustomerServiceImpl implements GenericService <Customer, Long> {

    private GenericDao<Customer, Long> customerDao;

    public CustomerServiceImpl(GenericDao<Customer, Long> customerDao) {
        this.customerDao = customerDao;
    }

    @Override
    public void save(Customer entity) {
        customerDao.save(entity);
    }

    @Override
    public Customer findById(Long id) {
        return customerDao.findById(id);
    }

    @Override
    public List<Customer> findAll() {
        return customerDao.findAll();
    }

    @Override
    public void update(Customer entity) {
        customerDao.update(entity);
    }

    @Override
    public void delete(Customer entity) {
        customerDao.delete(entity);
    }

}
