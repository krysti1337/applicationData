package com.example.reports.applicationdata.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
public class Product {

    @Id
    private String stockCode;
    private String description;
    @Column(precision = 10, scale = 2)
    private BigDecimal unitPrice;

//    @ManyToMany
//    @JoinTable(
//            name = "product_category",
//            joinColumns = @JoinColumn(name = "stock_code"),
//            inverseJoinColumns = @JoinColumn(name = "category_id")
//    )
//    private Set<Category> categories;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<Transaction> transactions;
}
