package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.model.*;
import com.example.reports.applicationdata.service.GenericService;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchProcessorConfig {

    @Bean
    public ItemProcessor<TransactionCsvRecord, Customer> customerProcessor() {
        return record -> {
            Customer customer = new Customer();
            customer.setCustomerId(record.getCustomerId());
            customer.setCountry(record.getCountry());
            return customer;
        };
    }

    @Bean
    public ItemProcessor<TransactionCsvRecord, Product> productProcessor() {
        return record -> {
            Product product = new Product();
            product.setStockCode(record.getStockCode());
            product.setDescription(record.getDescription());
            product.setUnitPrice(record.getUnitPrice());
            return product;
        };
    }

    @Bean
    public ItemProcessor<TransactionCsvRecord, Transaction> transactionProcessor(
            GenericService<Customer, Long> customerService,
            GenericService<Product, String> productService) {
        return record -> {
            Transaction t = new Transaction();
            t.setInvoiceNo(record.getInvoiceNo());
            t.setInvoiceDate(record.getInvoiceDate());
            t.setQuantity(record.getQuantity());
            t.setUnitPrice(record.getUnitPrice());
            t.setCustomer(customerService.findById(record.getCustomerId()));
            t.setProduct(productService.findById(record.getStockCode()));
            return t;
        };
    }
}