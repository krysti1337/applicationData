package com.example.reports.applicationdata.batch;
import lombok.*;

import java.math.BigDecimal;


@Data
public class TransactionCsvRecord {
    private String invoiceNo;
    private String stockCode;
    private String description;
    private int quantity;
    private String invoiceDate;
    private BigDecimal unitPrice;
    private String customerID;
    private String country;
}