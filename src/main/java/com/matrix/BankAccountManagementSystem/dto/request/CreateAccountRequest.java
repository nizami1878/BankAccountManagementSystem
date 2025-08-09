package com.matrix.BankAccountManagementSystem.dto.request;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CreateAccountRequest {
    private String accountNumber;
    private BigDecimal balance;
}
