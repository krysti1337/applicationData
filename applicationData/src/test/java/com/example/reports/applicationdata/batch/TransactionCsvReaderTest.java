package com.example.reports.applicationdata.batch;

import com.example.reports.applicationdata.exception.ValidationException;
import com.example.reports.applicationdata.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class TransactionCsvReaderTest {

    @Autowired
    private CsvReaderConfig csvReaderConfig;

    @Test
    public void whenReadCsvThenGetAllEntriesCorrect() throws Exception{

        Path tempCsvFile = Files.createTempFile("temp", ".csv");
        Files.write(tempCsvFile, List.of(
                "invoiceNo,stockCode,description,quantity,invoiceDate,unitPrice,customerID,country", // header
                "10001,STK1,Produs1,5,01/01/2025 10:00,10.50,123,Italy",
                "10002,STK2,Produs2,3,01/02/2025 11:00,20.00,124,Spain",
                "10003,STK3,Produs3,1,01/03/2025 12:00,5.00,125,Moldova"
        ));

        FlatFileItemReader<TransactionCsvRecord> reader = new FlatFileItemReaderBuilder<TransactionCsvRecord>()
                .name("transactionCsvReaderTest")
                .resource(new FileSystemResource(tempCsvFile.toFile()))
                .delimited().names("invoiceNo","stockCode","description","quantity",
                        "invoiceDate","unitPrice","customerID","country")
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {{ setTargetType(TransactionCsvRecord.class); }})
                .linesToSkip(1) // sar peste header
                .build();
        reader.open(new ExecutionContext());

        List<TransactionCsvRecord> result = new ArrayList<>();
        TransactionCsvRecord record;
        while ((record = reader.read()) != null) {
            result.add(record);
        }
        reader.close();

        assertEquals(3, result.size());
        assertEquals("10001", result.get(0).getInvoiceNo());
        assertEquals(5, result.get(0).getQuantity());
        assertEquals("Italy", result.get(0).getCountry());
        assertEquals("10002", result.get(1).getInvoiceNo());
        assertEquals(3, result.get(1).getQuantity());
        assertEquals("Spain", result.get(1).getCountry());
        assertEquals("10003", result.get(2).getInvoiceNo());
        assertEquals(1, result.get(2).getQuantity());
        assertEquals("Moldova", result.get(2).getCountry());
    }

    @Test
    public void whenProcessValidRecordsThenReturnPopulatedRecords() throws Exception{

        TransactionCsvRecord input = new TransactionCsvRecord();
        input.setInvoiceNo("536365");
        input.setStockCode("38411W");
        input.setDescription("Produs test");
        input.setQuantity(10);
        input.setInvoiceDate("01/01/2025 10:30");
        input.setUnitPrice(new BigDecimal("100.00"));
        input.setCustomerID("500");
        input.setCountry("Italy");

        TransactionCsvProcessor processor = new TransactionCsvProcessor();
        Transaction result = processor.process(input);

        assertNotNull(result, "Procesorul ar fi trebuit sa returneze un Transaction valid");
        assertEquals("536365", result.getInvoiceNo());
        assertEquals(10, result.getQuantity());
        assertEquals(LocalDateTime.parse("01/01/2025 10:30", DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm")),
                result.getInvoiceDate());
        assertEquals(500L, result.getCustomer().getCustomerId());
        assertEquals("Italy", result.getCustomer().getCountry());
        assertNotNull(result.getProduct());
        assertEquals("38411W", result.getProduct().getStockCode());
        assertEquals("Produs test", result.getProduct().getDescription());
        assertEquals(new BigDecimal("100.00"), result.getProduct().getUnitPrice());
    }

    @Test
    public void whenProcessInvalidRecordsThenThrowsException() throws Exception{

        TransactionCsvRecord input = new TransactionCsvRecord();
        input.setInvoiceNo(null);
        input.setStockCode("");
        input.setDescription("Produs test");
        input.setQuantity(0);
        input.setInvoiceDate("13/32/2025 00:00");
        input.setUnitPrice(new BigDecimal("100.00"));
        input.setCustomerID("");
        input.setCountry("Italy");

        TransactionCsvProcessor processor = new TransactionCsvProcessor();

        ValidationException ex = assertThrows(ValidationException.class, () -> processor.process(input));
        String mesaj = ex.getMessage();
        assertTrue(mesaj.contains("InvoiceNo is missing"));
        assertTrue(mesaj.contains("Quantity must be positive"));
        assertTrue(mesaj.contains("InvoiceDate is invalid"));
        assertTrue(mesaj.contains("CustomerID is missing"));
        assertTrue(mesaj.contains("StockCode is missing"));
    }
}
