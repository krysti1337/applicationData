package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.SkipListener;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.service.GenericService;

@Configuration
public class TransactionValidationProcessorConfig {

    private static final Logger logger = LoggerFactory.getLogger(TransactionValidationProcessorConfig.class);

    @Bean
    public ItemProcessor<TransactionCsvRecord, Transaction> validatedTransactionProcessor(
            GenericService<Customer, Long> customerService,
            GenericService<Product, String> productService) {

        return record -> {
            if (record.getInvoiceNo() == null || record.getInvoiceNo().isBlank()) return null;
            if (record.getQuantity() == null || record.getQuantity() <= 0) return null;
            if (record.getUnitPrice() == null || record.getUnitPrice().doubleValue() < 0) return null;
            if (record.getInvoiceDate() == null) return null;
            if (record.getCustomerId() == null || customerService.findById(record.getCustomerId()) == null) return null;
            if (record.getStockCode() == null || productService.findById(record.getStockCode()) == null) return null;

            Customer customer = customerService.findById(record.getCustomerId());
            Product product = productService.findById(record.getStockCode());

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

    @Bean
    public JobExecutionListener jobLoggerListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                logger.info("Job {} started.", jobExecution.getJobInstance().getJobName());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                logger.info("Job {} ended with status: {}.", jobExecution.getJobInstance().getJobName(), jobExecution.getStatus());
            }
        };
    }

    @Bean
    public SkipListener<TransactionCsvRecord, Transaction> transactionSkipListener() {
        return new SkipListener<>() {
            @Override
            public void onSkipInRead(Throwable t) {
                logger.warn("Skipped during read due to: {}", t.getMessage());
            }

            @Override
            public void onSkipInWrite(Transaction item, Throwable t) {
                logger.warn("Skipped during write. Item: {} due to: {}", item, t.getMessage());
            }

            @Override
            public void onSkipInProcess(TransactionCsvRecord item, Throwable t) {
                logger.warn("Skipped during process. CSV Record: {} due to: {}", item, t.getMessage());
            }
        };
    }
}

