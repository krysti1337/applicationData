package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.dao.impl.CustomerDaoImpl;
import com.example.reports.applicationdata.dao.impl.ProductDaoImpl;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.TransactionServiceImpl;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ExportTransactionTaskletTest {

    @Autowired
    private ExportTransactionTasklet exportTransactionTasklet;

    @Autowired
    private TransactionServiceImpl transactionService;

    @Autowired
    private CustomerDaoImpl customerDao;

    @Autowired
    private ProductDaoImpl productDao;

    @Test
    public void whenTaskletRunsThenGeneratesCsvFileWithTransactions() throws Exception {
        Customer customer = new Customer();
        customer.setCustomerId(100L);
        customer.setCountry("Italy");
        customerDao.save(customer);

        Product product = new Product();
        product.setStockCode("10001");
        product.setDescription("Product Export");
        product.setUnitPrice(new BigDecimal("150.00"));
        productDao.save(product);

        Transaction transaction = new Transaction();
        transaction.setInvoiceNo("INV-001");
        transaction.setQuantity(3);
        transaction.setInvoiceDate(LocalDateTime.of(2025, 6, 1, 10, 30));
        transaction.setCustomer(customer);
        transaction.setProduct(product);
//        transaction.setUnitPrice(product.getUnitPrice());

        transactionService.save(transaction);


        exportTransactionTasklet.execute(null, null);

        // Assert – verifica fisierul CSV generat
        File csvFile = new File("exports/exported_transactions.csv");
        assertTrue(csvFile.exists(), "Fișierul CSV ar trebui să fie creat.");

        List<String> lines = Files.readAllLines(csvFile.toPath());
        assertFalse(lines.isEmpty(), "Fișierul CSV nu ar trebui să fie gol.");
        assertEquals(2, lines.size(), "CSV ar trebui să aibă header + 1 rând de date.");

        assertEquals(
                "InvoiceNo,Quantity,InvoiceDate,CustomerID,Country,StockCode,Description,UnitPrice",
                lines.get(0)
        );

        String[] values = lines.get(1).split(",");
        assertEquals("INV-001", values[0]);
        assertEquals("3", values[1]);
        assertTrue(values[2].startsWith("2025-06-01"), "Data trebuie să fie în format ISO (YYYY-MM-DD)");
        assertEquals("100", values[3]);
        assertEquals("Italy", values[4]);
        assertEquals("10001", values[5]);
        assertEquals("Product Export", values[6]);
        assertEquals("150.00", values[7]);
    }
}
