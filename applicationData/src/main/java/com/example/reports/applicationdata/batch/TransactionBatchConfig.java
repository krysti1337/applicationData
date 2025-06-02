package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.model.Transaction;
import com.example.reports.applicationdata.model.Customer;
import com.example.reports.applicationdata.model.Product;
import com.example.reports.applicationdata.service.GenericService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.builder.SimpleJobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;
@Configuration
public class TransactionBatchConfig {

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public FlatFileItemReader<TransactionCsvRecord> transactionReader(){
        return new FlatFileItemReaderBuilder<TransactionCsvRecord>()
                .name("transactionItemReader")
                .resource(new ClassPathResource("transactions.csv"))
                .delimited()
                .names("invoiceNo", "stockCode", "description", "quantity", "invoiceDate", "unitPrice", "customerId", "country")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TransactionCsvRecord.class);
                }})
                .build();
    }

    @Bean
    public ItemProcessor<TransactionCsvRecord, Transaction> transactionProcessor(GenericService<Customer, Long> customerService, GenericService<Product, String> productService) {
        return record -> {
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
    public JpaItemWriter<Transaction> transactionWriter() {
        JpaItemWriter<Transaction> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManager.getEntityManagerFactory());
        return writer;
    }

    @Bean
    public Step transactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                                FlatFileItemReader<TransactionCsvRecord> reader,
                                ItemProcessor<TransactionCsvRecord, Transaction> processor,
                                JpaItemWriter<Transaction> writer) {
        return new StepBuilder("transactionStep", jobRepository)
                .<TransactionCsvRecord, Transaction>chunk(100, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean
    public Job transactionImportJob(JobRepository jobRepository, Step transactionStep) {
        return new JobBuilder("transactionImportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(transactionStep)
                .build();
    }
}
