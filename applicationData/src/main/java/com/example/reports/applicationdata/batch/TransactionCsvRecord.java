package com.example.reports.applicationdata.batch;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionCsvRecord {
    private String invoiceNo;
    private String stockCode;
    private String description;
    private int quantity;
    private String invoiceDate; // pastram ca String pentru a-l converti manual in procesor
    private BigDecimal unitPrice;
    private String customerID;
    private String country;
}