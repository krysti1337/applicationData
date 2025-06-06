//package com.example.reports.applicationdata.dao.impl;
//
//import com.example.reports.applicationdata.model.Category;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;;
//
//
//import java.util.List;
//
//@Repository
//@Transactional
//public class CategoryDaoImpl implements GenericDao<Category, Long> {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public void save(Category entity) {
//        entityManager.persist(entity);
//    }
//
//    @Override
//    public Category findById(Long id) {
//        return entityManager.find(Category.class, id);
//    }
//
//    @Override
//    public List<Category> findAll() {
//        return entityManager.createQuery("from Category", Category.class).getResultList();
//    }
//
//    @Override
//    public void update(Category entity) {
//        entityManager.merge(entity);
//    }
//
//    @Override
//    public void delete(Category entity) {
//        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
//    }
//}
