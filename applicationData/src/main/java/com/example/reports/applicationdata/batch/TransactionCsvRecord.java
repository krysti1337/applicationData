package com.example.reports.applicationdata.batch;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionCsvRecord {
    private String invoiceNo;
    private String stockCode;
    private String description;
    private Integer quantity;
    private LocalDateTime invoiceDate;
    private BigDecimal unitPrice;
    private Long customerId;
    private String country;

    public String getInvoiceNo() { return invoiceNo; }
    public void setInvoiceNo(String invoiceNo) { this.invoiceNo = invoiceNo; }
    public String getStockCode() { return stockCode; }
    public void setStockCode(String stockCode) { this.stockCode = stockCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public LocalDateTime getInvoiceDate() { return invoiceDate; }
    public void setInvoiceDate(LocalDateTime invoiceDate) { this.invoiceDate = invoiceDate; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}