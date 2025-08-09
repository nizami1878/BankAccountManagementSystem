package com.matrix.BankAccountManagementSystem.dto.request;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateCustomerRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate birthDate;
}
