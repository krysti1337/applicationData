package com.example.reports.applicationdata.dao.impl;

import java.util.List;

public interface GenericDao<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(T entity);
}
