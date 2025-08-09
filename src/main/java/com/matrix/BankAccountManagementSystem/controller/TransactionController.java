package com.matrix.BankAccountManagementSystem.controller;

import com.matrix.BankAccountManagementSystem.dto.TransactionDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateTransactionRequest;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionStatus;
import com.matrix.BankAccountManagementSystem.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    @PostMapping
    public ResponseEntity<TransactionDto> createTransaction(@RequestBody CreateTransactionRequest request) {
        return ResponseEntity.ok(transactionService.createTransaction(request));
    }

    @GetMapping("/{transaction_id}")
    public ResponseEntity<TransactionDto> findById(@PathVariable Integer transaction_id) {
        return ResponseEntity.ok(transactionService.findById(transaction_id));
    }

    @PostMapping("/purchase")
    public ResponseEntity<TransactionDto> purchase(@RequestParam String accountNumber, @RequestParam Integer amount) {
        return ResponseEntity.ok(transactionService.purchase(accountNumber, amount));
    }

    @PostMapping("/top-up")
    public ResponseEntity<TransactionDto> topUp(@RequestParam String accountNumber, @RequestParam Integer amount) {
        return ResponseEntity.ok(transactionService.topUp(accountNumber, amount));
    }

    @PutMapping("/refund/{transaction_id}")
    public ResponseEntity<TransactionDto> refund(@PathVariable Integer transaction_id) {
        return ResponseEntity.ok(transactionService.refund(transaction_id));
    }

    @PutMapping("/change-status/{transaction_id}")
    public ResponseEntity<TransactionDto> changePaymentStatus(
            @PathVariable Integer transaction_id,
            @RequestParam TransactionStatus status) {
        return ResponseEntity.ok(transactionService.changePaymentStatus(transaction_id, status));
    }
}
