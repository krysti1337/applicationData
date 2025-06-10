package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.dao.impl.CustomerDaoImpl;
import com.example.reports.applicationdata.dao.impl.ProductDaoImpl;
import com.example.reports.applicationdata.dao.impl.TransactionDaoImpl;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class MultiEntityWriter implements ItemWriter<Transaction> {

    private final CustomerDaoImpl customerDao;
    private final ProductDaoImpl productDao;
    private final TransactionDaoImpl transactionDao;

    @Autowired
    public MultiEntityWriter(CustomerDaoImpl customerDao, ProductDaoImpl productDao, TransactionDaoImpl transactionDao) {
        this.customerDao = customerDao;
        this.productDao = productDao;
        this.transactionDao = transactionDao;
    }

    @Override
    public void write(Chunk<? extends Transaction> chunk) {
        for (Transaction transaction : chunk) {
            Customer customer = customerDao.findById(transaction.getCustomer().getCustomerId());
            if (customer == null) {
                customerDao.save(transaction.getCustomer());
            } else {
                transaction.setCustomer(customer);
            }

            Product product = productDao.findById(transaction.getProduct().getStockCode());
            if (product == null) {
                productDao.save(transaction.getProduct());
            } else {
                transaction.setProduct(product);
            }

            transactionDao.save(transaction);
        }
    }
}