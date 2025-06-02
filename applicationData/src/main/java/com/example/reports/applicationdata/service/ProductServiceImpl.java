package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.GenericDao;
import com.example.reports.applicationdata.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements GenericService<Product, String> {

    private final GenericDao<Product, String> productDao;

    public ProductServiceImpl(GenericDao<Product, String> productDao) {
        this.productDao = productDao;
    }

    @Override
    public void save(Product entity) {
        productDao.save(entity);
    }

    @Override
    public Product findById(String id) {
        return productDao.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public void update(Product entity) {
        productDao.update(entity);
    }

    @Override
    public void delete(Product entity) {
        productDao.delete(entity);
    }
}
