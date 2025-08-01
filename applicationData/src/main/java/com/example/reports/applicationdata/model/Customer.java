package com.example.reports.applicationdata.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
public class Customer {
    @Id
    private Long customerId;

    private String country;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "profile_id")
    private Profile profile;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    @JsonManagedReference(value = "customer-transaction")
    private List<Transaction> transactions;

    public Customer(String customerID, String country) {
    }
}
