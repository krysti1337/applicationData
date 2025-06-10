package com.example.reports.applicationdata.dao.impl;


import com.example.reports.applicationdata.model.Customer;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CustomerDaoImplTest {

    @Autowired
    private CustomerDaoImpl customerDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSaveAndFindById() {
        Customer customer = new Customer();
        customer.setCustomerId(1001L);
        customer.setCountry("Italy");

        customerDao.save(customer);
        entityManager.flush(); // fortam scrierea in DB

        Customer found = customerDao.findById(1001L);
        assertNotNull(found);
        assertEquals("Italy", found.getCountry());
    }

    @Test
    public void testFindAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1001L);
        customer1.setCountry("Italy");

        Customer customer2 = new Customer();
        customer2.setCustomerId(1002L);
        customer2.setCountry("Germany");

        customerDao.save(customer1);
        customerDao.save(customer2);
        entityManager.flush();

        List<Customer> customers = customerDao.findAll();
        assertEquals(2, customers.size());
    }

    @Test
    public void testUpdateCustomerCountryName() {
        Customer customer = new Customer();
        customer.setCustomerId(1001L);
        customer.setCountry("Germany");

        customerDao.save(customer);
        entityManager.flush();

        customer.setCountry("Moldova");
        customer.setCustomerId(1001L);
        customerDao.update(customer);
        entityManager.flush();

        Customer updated = customerDao.findById(customer.getCustomerId());
        assertEquals("Moldova", updated.getCountry());
    }

    @Test
    public void testDeleteCustomer() {
        Customer customer = new Customer();
        customer.setCustomerId(1003L);
        customer.setCountry("Romania");

        customerDao.save(customer);
        entityManager.flush();

        customerDao.delete(customer);
        entityManager.flush();

        Customer deleted = customerDao.findById(1003L);
        assertNull(deleted);
    }
}
