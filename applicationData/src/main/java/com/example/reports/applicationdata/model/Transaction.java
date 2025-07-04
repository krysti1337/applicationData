package com.example.reports.applicationdata.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String invoiceNo;
    private Integer quantity;
    private LocalDateTime invoiceDate;
//    private BigDecimal unitPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "product-transaction")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference(value = "customer-transaction")
    private Customer customer;
}
