package com.example.reports.applicationdata.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionImportJobConfig {

    @Bean
    public Job transactionImportJob(JobRepository jobRepository,
                                    Step importCustomersStep,
                                    Step importProductsStep,
                                    Step transactionStep) {
        return new JobBuilder("transactionImportJob", jobRepository)
                .start(importCustomersStep)
                .next(importProductsStep)
                .next(transactionStep)
                .build();
    }
}