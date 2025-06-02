package com.example.reports.applicationdata.service;

import com.example.reports.applicationdata.dao.impl.GenericDao;
import com.example.reports.applicationdata.model.Category;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements GenericService<Category, Long> {

    private final GenericDao<Category, Long> categoryDao;

    public CategoryServiceImpl(GenericDao<Category, Long> categoryDao) {
        this.categoryDao = categoryDao;
    }

    @Override
    public void save(Category entity) {
        categoryDao.save(entity);
    }

    @Override
    public Category findById(Long id) {
        return categoryDao.findById(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryDao.findAll();
    }

    @Override
    public void update(Category entity) {
        categoryDao.update(entity);
    }

    @Override
    public void delete(Category entity) {
        categoryDao.delete(entity);
    }
}

