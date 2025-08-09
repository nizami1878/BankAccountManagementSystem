package com.matrix.BankAccountManagementSystem.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@Entity
@NoArgsConstructor
@Table(name = "accounts")
public class Account extends BaseEntity {
    private String accountNumber;
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;
    @OneToMany(mappedBy = "account",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private List<Transaction> transactions;


}
