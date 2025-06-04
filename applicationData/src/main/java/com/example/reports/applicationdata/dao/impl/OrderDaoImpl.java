//package com.example.reports.applicationdata.dao.impl;
//
//import com.example.reports.applicationdata.model.Order;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//
//import org.springframework.stereotype.Repository;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Repository
//@Transactional
//public class OrderDaoImpl implements GenericDao<Order, Long> {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public void save(Order entity) {
//        entityManager.persist(entity);
//    }
//
//    @Override
//    public Order findById(Long id) {
//        return entityManager.find(Order.class, id);
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return entityManager.createQuery("from Order", Order.class).getResultList();
//    }
//
//    @Override
//    public void update(Order entity) {
//        entityManager.merge(entity);
//    }
//
//    @Override
//    public void delete(Order entity) {
//        entityManager.remove(entityManager.contains(entity) ? entity : entityManager.merge(entity));
//    }
//}
//
