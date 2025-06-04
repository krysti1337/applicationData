//package com.example.reports.applicationdata.service;
//
//import com.example.reports.applicationdata.dao.impl.GenericDao;
//import com.example.reports.applicationdata.model.Order;
//import jakarta.transaction.Transactional;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//@Transactional
//public class OrderServiceImpl implements GenericService<Order, Long> {
//
//    private final GenericDao<Order, Long> orderDao;
//
//    public OrderServiceImpl(GenericDao<Order, Long> orderDao) {
//        this.orderDao = orderDao;
//    }
//
//    @Override
//    public void save(Order entity) {
//        orderDao.save(entity);
//    }
//
//    @Override
//    public Order findById(Long id) {
//        return orderDao.findById(id);
//    }
//
//    @Override
//    public List<Order> findAll() {
//        return orderDao.findAll();
//    }
//
//    @Override
//    public void update(Order entity) {
//        orderDao.update(entity);
//    }
//
//    @Override
//    public void delete(Order entity) {
//        orderDao.delete(entity);
//    }
//}
