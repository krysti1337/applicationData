package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.exception.ValidationException;
import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.SkipListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@EnableBatchProcessing
public class BatchConfig {

    @Autowired
    private CsvReaderConfig csvReaderConfig;

    @Autowired
    private TransactionCsvProcessor processor;

    @Autowired
    private MultiEntityWriter writer;

    @Autowired
    private ExportTransactionTasklet exportTasklet;

    @Bean
    public Step importCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("importCsvStep", jobRepository)
                .<TransactionCsvRecord, Transaction>chunk(100, transactionManager)
                .reader(csvReaderConfig.transactionCsvReader())
                .processor(processor)
                .writer(writer)
                .faultTolerant()  // gestionarea erorilor
                .skip(ValidationException.class)  // sari peste cele esuate
                .skipLimit(100)  // limita max pentru skip
                .build();
    }

    @Bean
    public Step exportTransactionsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("exportTransactionsStep", jobRepository)
                .tasklet(exportTasklet, transactionManager)
                .build();
    }

    @Bean(name = "transactionImportJob")
    public Job importTransactionJob(JobRepository jobRepository, Step importCsvStep, Step exportTransactionsStep) {
        return new JobBuilder("transactionImportJob", jobRepository)
                .start(importCsvStep)
                .next(exportTransactionsStep)
                .build();
    }

    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    //Tasklet STEP and JOB

    @Bean
    public Step exportTransactionStep(JobRepository jobRepository, PlatformTransactionManager transactionManager, ExportTransactionTasklet tasklet) {
        return new StepBuilder("exportTransactionStep", jobRepository)
                .tasklet(tasklet, transactionManager)
                .build();
    }

    @Bean(name = "exportTransactionJob")
    public Job exportTransactionJob(JobRepository jobRepository, Step exportTransactionStep) {
        return new JobBuilder("exportTransactionJob", jobRepository)
                .start(exportTransactionStep)
                .build();
    }
}
