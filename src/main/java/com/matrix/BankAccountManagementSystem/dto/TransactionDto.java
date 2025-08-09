package com.matrix.BankAccountManagementSystem.dto;

import com.matrix.BankAccountManagementSystem.model.entity.TransactionStatus;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionType;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionDto {
    private Integer id;
    private String accountNumber;
    private double amount;
    private TransactionType type;
    private TransactionStatus status;

}
