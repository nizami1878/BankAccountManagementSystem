package com.matrix.BankAccountManagementSystem.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AccountDto {
    private String accountNumber;
    private BigDecimal balance;
    private Integer customer_id;
    private Integer id;
}
