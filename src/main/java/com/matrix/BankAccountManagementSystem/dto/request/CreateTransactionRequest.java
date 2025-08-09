package com.matrix.BankAccountManagementSystem.dto.request;

import com.matrix.BankAccountManagementSystem.model.entity.TransactionStatus;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateTransactionRequest {
    private double amount;
    private String accountNumber;
    private TransactionType type;
    private TransactionStatus status;
}
