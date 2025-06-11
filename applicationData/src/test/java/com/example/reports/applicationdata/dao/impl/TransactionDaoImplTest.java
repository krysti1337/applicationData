package com.example.reports.applicationdata.dao.impl;


import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class TransactionDaoImplTest {

    @Autowired
    private TransactionDaoImpl transactionDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void testSaveAndFindById(){
        Customer customer = new Customer();
        customer.setCustomerId(100L);
        customer.setCountry("Italy");

        Product product = new Product();
        product.setStockCode("200L");
        product.setDescription("Product Description");

        entityManager.persist(customer);
        entityManager.persist(product);

        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");
        transaction.setQuantity(2);
        transaction.setInvoiceDate(LocalDateTime.now());
        transaction.setUnitPrice(new BigDecimal("999.99"));
        transaction.setCustomer(customer);
        transaction.setProduct(product);

        transactionDao.save(transaction);
        entityManager.flush();

        Transaction found  = transactionDao.findById(transaction.getId());
        assertNotNull(found);
        assertEquals("INV-001", found.getInvoiceNo());
        assertEquals(2, found.getQuantity());
        assertEquals(new BigDecimal("999.99"), found.getUnitPrice());
        assertEquals(customer, found.getCustomer());
        assertEquals(product, found.getProduct());
    }

    @Test
    public void testFindAllTransactions(){
        Customer customer = new Customer();
        customer.setCustomerId(101L);
        customer.setCountry("Romania");
        entityManager.persist(customer);

        Product product = new Product();
        product.setStockCode("201L");
        product.setDescription("Monitor Description");
        entityManager.persist(product);

        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");
        transaction.setQuantity(1);
        transaction.setInvoiceDate(LocalDateTime.now());
        transaction.setUnitPrice(new BigDecimal("150.99"));
        transaction.setCustomer(customer);
        transaction.setProduct(product);
        entityManager.persist(transaction);

        Transaction transaction2 = new Transaction();
        transaction2.setInvoiceNo("INV-002");
        transaction2.setQuantity(2);
        transaction2.setInvoiceDate(LocalDateTime.now());
        transaction2.setUnitPrice(new BigDecimal("300.00"));
        transaction2.setCustomer(customer);
        transaction2.setProduct(product);

        transactionDao.save(transaction2);

        entityManager.flush();
        entityManager.clear(); //curatam contextul pentru a forta fetch din DB

        List<Transaction> transactions = transactionDao.findAll();

        //Verificam
        assertEquals(2, transactions.size());

        for(Transaction t: transactions){
            assertNotNull(t.getCustomer());
            assertNotNull(t.getProduct());
            assertTrue(t.getInvoiceNo().startsWith("INV-"));
        }
    }

    @Test
    public void testUpdateTransaction(){
        Customer customer = new Customer();
        customer.setCustomerId(100L);
        customer.setCountry("Romania");

        Product product = new Product();
        product.setStockCode("200L");
        product.setDescription("Product Description");
        entityManager.persist(customer);
        entityManager.persist(product);

        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");
        transaction.setQuantity(2);
        transaction.setInvoiceDate(LocalDateTime.now());
        transaction.setUnitPrice(new BigDecimal("999.99"));
        transaction.setCustomer(customer);
        transaction.setProduct(product);
        transactionDao.save(transaction);

        entityManager.flush();

        transaction.setInvoiceNo("INV-002");
        transaction.setQuantity(3);

        transactionDao.update(transaction);
        entityManager.flush();

        Transaction updated = transactionDao.findById(transaction.getId());
        assertEquals("INV-002", updated.getInvoiceNo());
        assertEquals(3, updated.getQuantity());

    }

    @Test
    public void testDeleteTransaction(){
        Customer customer = new Customer();
        customer.setCustomerId(100L);
        customer.setCountry("Moldova");
        entityManager.persist(customer);

        Product product = new Product();
        product.setStockCode("200L");
        product.setDescription("Product Description");
        entityManager.persist(product);

        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");
        transaction.setQuantity(2);
        transaction.setInvoiceDate(LocalDateTime.now());
        transaction.setUnitPrice(new BigDecimal("999.99"));
        transaction.setCustomer(customer);
        transaction.setProduct(product);
        transactionDao.save(transaction);

        entityManager.flush();

        transactionDao.delete(transaction);
        entityManager.flush();

        Transaction deleted = transactionDao.findById(transaction.getId());
        assertNull(deleted);
    }
}
