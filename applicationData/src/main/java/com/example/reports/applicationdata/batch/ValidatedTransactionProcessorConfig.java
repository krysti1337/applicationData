package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.service.GenericService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ValidatedTransactionProcessorConfig {

    @Bean(name = "validatedTransactionProcessor")
    public ItemProcessor<TransactionCsvRecord, Transaction> validatedTransactionProcessor(
            GenericService<Customer, Long> customerService,
            GenericService<Product, String> productService) {

        return record -> {
            if (record.getInvoiceNo() == null || record.getInvoiceNo().isBlank()) return null;
            if (record.getQuantity() == null || record.getQuantity() <= 0) return null;
            if (record.getUnitPrice() == null || record.getUnitPrice().doubleValue() < 0) return null;
            if (record.getInvoiceDate() == null) return null;

            Customer customer = customerService.findById(record.getCustomerId());
            if (customer == null) return null;

            Product product = productService.findById(record.getStockCode());
            if (product == null) return null;

            Transaction transaction = new Transaction();
            transaction.setInvoiceNo(record.getInvoiceNo());
            transaction.setQuantity(record.getQuantity());
            transaction.setInvoiceDate(record.getInvoiceDate());
            transaction.setUnitPrice(record.getUnitPrice());
            transaction.setCustomer(customer);
            transaction.setProduct(product);

            return transaction;
        };
    }
}