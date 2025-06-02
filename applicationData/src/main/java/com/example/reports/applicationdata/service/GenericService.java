package com.example.reports.applicationdata.service;

import java.util.List;

public interface GenericService<T, ID> {
    void save(T entity);
    T findById(ID id);
    List<T> findAll();
    void update(T entity);
    void delete(T entity);
}