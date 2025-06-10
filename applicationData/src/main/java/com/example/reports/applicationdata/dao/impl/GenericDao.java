package com.example.reports.applicationdata.dao.impl;

import java.util.List;
import java.util.Set;

public interface GenericDao<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void saveAll(Set<T> entities);
    void update(T entity);
    void delete(T entity);
}
