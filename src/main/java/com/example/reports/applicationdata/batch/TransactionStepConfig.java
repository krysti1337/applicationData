package com.example.reports.applicationdata.batch;
import com.example.reports.applicationdata.batch.TransactionCsvRecord;
import com.example.reports.applicationdata.model.Transaction;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.repository.JobRepository;

@Configuration
public class TransactionStepConfig {

    @Bean
    public Step transactionStep(JobRepository jobRepository,
                                PlatformTransactionManager transactionManager,
                                FlatFileItemReader<TransactionCsvRecord> transactionReader,
                                ItemProcessor<TransactionCsvRecord, Transaction> transactionProcessor,
                                JpaItemWriter<Transaction> transactionWriter) {
        return new StepBuilder("transactionStep", jobRepository)
                .<TransactionCsvRecord, Transaction>chunk(100, transactionManager)
                .reader(transactionReader)
                .processor(transactionProcessor)
                .writer(transactionWriter)
                .build();
    }
}

