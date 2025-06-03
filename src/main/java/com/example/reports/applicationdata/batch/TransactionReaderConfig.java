package com.example.reports.applicationdata.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class TransactionReaderConfig {

    @Bean(name = "transactionReader1")
    public FlatFileItemReader<TransactionCsvRecord> transactionReader1() {
        return createReader("transactionReader1");
    }

    @Bean(name = "transactionReader2")
    public FlatFileItemReader<TransactionCsvRecord> transactionReader2() {
        return createReader("transactionReader2");
    }

    @Bean(name = "transactionReader3")
    public FlatFileItemReader<TransactionCsvRecord> transactionReader3() {
        return createReader("transactionReader3");
    }

    private FlatFileItemReader<TransactionCsvRecord> createReader(String name) {
        return new FlatFileItemReaderBuilder<TransactionCsvRecord>()
                .name(name)
                .resource(new ClassPathResource("transactions.csv"))
                .delimited()
                .names("invoiceNo", "stockCode", "description", "quantity", "invoiceDate", "unitPrice", "customerId", "country")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TransactionCsvRecord.class);
                }})
                .build();
    }
}
