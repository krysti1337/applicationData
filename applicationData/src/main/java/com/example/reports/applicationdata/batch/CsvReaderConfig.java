package com.example.reports.applicationdata.batch;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class CsvReaderConfig {

    @Bean
    public FlatFileItemReader<TransactionCsvRecord> transactionCsvReader() {
        return new FlatFileItemReaderBuilder<TransactionCsvRecord>()
                .name("transactionCsvReader")
                .resource(new ClassPathResource("transactions.csv"))
                .delimited()
                .names("invoiceNo", "stockCode", "description", "quantity", "invoiceDate",
                        "unitPrice", "customerID", "country")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{
                    setTargetType(TransactionCsvRecord.class);
                }})
                .linesToSkip(1) // Sari peste header
                .strict(false)
                .build();
    }
}
