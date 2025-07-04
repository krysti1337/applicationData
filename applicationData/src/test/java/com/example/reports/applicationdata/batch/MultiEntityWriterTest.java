package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.dao.impl.CustomerDaoImpl;
import com.example.reports.applicationdata.dao.impl.ProductDaoImpl;
import com.example.reports.applicationdata.dao.impl.TransactionDaoImpl;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.ui.CustomerView;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MultiEntityWriterTest {

    @Autowired
    private MultiEntityWriter multiEntityWriter;

    @Autowired
    private CustomerDaoImpl customerDao;

    @Autowired
    private ProductDaoImpl productDao;

    @Autowired
    private TransactionDaoImpl transactionDao;

    @Autowired
    private EntityManager entityManager;

    @Test
    void whenWritingDuplicateTransactionsThenDontDuplicateAssociatedEntities(){

        Customer customer1 = new Customer();
        customer1.setCustomerId(100L);
        customer1.setCountry("Italy");

        Product product1 = new Product();
        product1.setStockCode("P1001");
        product1.setDescription("Product 1 test");
        product1.setUnitPrice(new BigDecimal("50.00"));

        Transaction transaction1 = new Transaction();
        transaction1.setInvoiceNo("INV-001");
        transaction1.setQuantity(5);
        transaction1.setInvoiceDate(LocalDateTime.now());
        transaction1.setCustomer(customer1);
        transaction1.setProduct(product1);

        Customer customer2 = new Customer();
        customer2.setCustomerId(100L); //same id as in customer1
        customer2.setCountry("Italy");

        Product product2 = new Product();
        product2.setStockCode("P1002");
        product2.setDescription("Product 2 test");
        product2.setUnitPrice(new BigDecimal("50.00"));

        Transaction transaction2 = new Transaction();
        transaction2.setInvoiceNo("INV-002");
        transaction2.setQuantity(7);
        transaction2.setInvoiceDate(LocalDateTime.now().plusDays(1));
        transaction2.setCustomer(customer2);
        transaction2.setProduct(product2);

        multiEntityWriter.write(new Chunk<>(List.of(transaction1, transaction2)));
        entityManager.flush();
        entityManager.clear();

        List<Customer> customers = customerDao.findAll();
        List<Product> products = productDao.findAll();
        List<Transaction> transactions = transactionDao.findAll();

        assertEquals(1, customers.size());
        assertEquals(100L, customers.get(0).getCustomerId());
        assertEquals(2, products.size());
        assertEquals(2, transactions.size());

        assertEquals(customers.get(0), transactions.get(0).getCustomer());
        assertEquals(customers.get(0), transactions.get(1).getCustomer());

        assertEquals(products.get(0).getStockCode(), transactions.get(0).getProduct().getStockCode());
        assertEquals(products.get(1).getStockCode(), transactions.get(1).getProduct().getStockCode());

    }
}
