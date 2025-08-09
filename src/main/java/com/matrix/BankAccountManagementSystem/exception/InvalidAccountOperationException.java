package com.matrix.BankAccountManagementSystem.exception;

public class InvalidAccountOperationException extends RuntimeException {
    public InvalidAccountOperationException(String message) {
        super(message);
    }
}
