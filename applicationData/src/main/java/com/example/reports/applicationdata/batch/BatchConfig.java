package com.example.reports.applicationdata.batch;


import com.example.reports.applicationdata.batch.TransactionCsvRecord;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Bean
    public FlatFileItemReader<TransactionCsvRecord> reader() {
        return new FlatFileItemReaderBuilder<TransactionCsvRecord>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("transactions.csv"))
                .linesToSkip(1)  // <--- Asta e linia salvatoare
                .delimited()
                .names("InvoiceNo", "StockCode", "Description", "Quantity", "InvoiceDate", "UnitPrice", "CustomerID", "Country")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TransactionCsvRecord.class);
                }})
                .build();
    }

    @Bean
    public JpaItemWriter<Customer> customerWriter(EntityManagerFactory emf) {
        JpaItemWriter<Customer> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public JpaItemWriter<Product> productWriter(EntityManagerFactory emf) {
        JpaItemWriter<Product> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public JpaItemWriter<Transaction> transactionWriter(EntityManagerFactory emf) {
        JpaItemWriter<Transaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(emf);
        return writer;
    }

    @Bean
    public ItemProcessor<TransactionCsvRecord, Customer> customerProcessor() {
        return record -> {
            Customer customer = new Customer();
            customer.setCustomerId(Long.parseLong(record.getCustomerID()));
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
    public ItemProcessor<TransactionCsvRecord, Transaction> transactionProcessor() {
        return record -> {
            Transaction transaction = new Transaction();
            transaction.setInvoiceNo(record.getInvoiceNo());
            transaction.setQuantity(record.getQuantity());

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d/yyyy H:mm");
            LocalDateTime invoiceDate = LocalDateTime.parse(record.getInvoiceDate(), formatter);
            transaction.setInvoiceDate(invoiceDate);

            Customer customer = new Customer();
            customer.setCustomerId(Long.parseLong(record.getCustomerID()));
            transaction.setCustomer(customer);

            Product product = new Product();
            product.setStockCode(record.getStockCode());
            transaction.setProduct(product);

            return transaction;
        };
    }


    @Bean
    public Step customerStep(JobRepository jobRepository,
                             PlatformTransactionManager transactionManager,
                             FlatFileItemReader<TransactionCsvRecord> reader,
                             ItemProcessor<TransactionCsvRecord, Customer> customerProcessor,
                             JpaItemWriter<Customer> writer) {
        return new StepBuilder("customerStep", jobRepository)
                .<TransactionCsvRecord, Customer>chunk(100, transactionManager)
                .reader(reader)
                .processor(customerProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step productStep(JobRepository jobRepository,
                            PlatformTransactionManager transactionManager,
                            FlatFileItemReader<TransactionCsvRecord> reader,
                            ItemProcessor<TransactionCsvRecord, Product> productProcessor,
                            JpaItemWriter<Product> writer) {
        return new StepBuilder("productStep", jobRepository)
                .<TransactionCsvRecord, Product>chunk(100, transactionManager)
                .reader(reader)
                .processor(productProcessor)
                .writer(writer)
                .build();
    }

    @Bean
    public Step transactionStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                FlatFileItemReader<TransactionCsvRecord> reader,
                                ItemProcessor<TransactionCsvRecord, Transaction> transactionProcessor,
                                JpaItemWriter<Transaction> writer) {
        return new StepBuilder("transactionStep", jobRepository)
                .<TransactionCsvRecord, Transaction>chunk(100, transactionManager)
                .reader(reader)
                .processor(transactionProcessor)
                .writer(writer)
                .build();
    }

    @Bean(name = "transactionImportJob")
    public Job importTransactionJob(JobRepository jobRepository,
                                    Step customerStep,
                                    Step productStep,
                                    Step transactionStep) {
        return new JobBuilder("importTransactionJob", jobRepository)
                .start(customerStep)
                .next(productStep)
                .next(transactionStep)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}
