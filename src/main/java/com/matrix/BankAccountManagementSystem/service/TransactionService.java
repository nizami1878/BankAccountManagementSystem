package com.matrix.BankAccountManagementSystem.service;

import com.matrix.BankAccountManagementSystem.dto.TransactionDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateTransactionRequest;
import com.matrix.BankAccountManagementSystem.mapper.TransactionMapper;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.Transaction;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionStatus;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionType;
import com.matrix.BankAccountManagementSystem.model.repository.AccountRepository;
import com.matrix.BankAccountManagementSystem.model.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransactionMapper transactionMapper;
    private final AccountService accountService;
    public TransactionDto createTransaction(CreateTransactionRequest request) {
        System.out.println("Looking for account with number: " + request.getAccountNumber());
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account with number " + request.getAccountNumber() + " not found"));
        if (request.getType() == TransactionType.PURCHASE && account.getBalance().compareTo(BigDecimal.valueOf(request.getAmount())) < 0) {
            throw new RuntimeException("Insufficient balance for the purchase");
        }


        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setStatus(TransactionStatus.PENDING);

        transactionRepository.save(transaction);
        return transactionMapper.toDto(transaction);
    }
    public TransactionDto findById(Integer transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found!"));
        return transactionMapper.toDto(transaction);
    }
    public TransactionDto purchase(String accountNumber, Integer amount) {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);
        request.setType(TransactionType.PURCHASE);
        request.setStatus(TransactionStatus.PENDING);

        return createTransaction(request);
    }
    public TransactionDto topUp(String accountNumber, Integer amount) {
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);
        request.setType(TransactionType.TOP_UP);
        return createTransaction(request);
    }

    public TransactionDto refund(Integer transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found!"));

        if (transaction.getType() != TransactionType.PURCHASE || transaction.getStatus() != TransactionStatus.SUCCESS) {
            throw new RuntimeException("Refund is only allowed for successful purchases!");
        }
        Account account = transaction.getAccount();
        BigDecimal refundAmount = BigDecimal.valueOf(transaction.getAmount());
        account.setBalance(account.getBalance().add(refundAmount));
        accountRepository.save(account);
        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountNumber(account.getAccountNumber());
        request.setAmount(refundAmount.intValueExact());
        request.setType(TransactionType.REFUND);
        transaction.setStatus(TransactionStatus.REFUNDED);
        TransactionDto refundTransaction = createTransaction(request);
        transaction.setStatus(TransactionStatus.REFUNDED);
        transactionRepository.save(transaction);

        return refundTransaction;
    }

    public TransactionDto changePaymentStatus(Integer transactionId, TransactionStatus status) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found!"));

        transaction.setStatus(status);
        transactionRepository.save(transaction);

        return transactionMapper.toDto(transaction);//
    }
    @Scheduled(cron = "0 20 23 * * ?")
    public void updatePendingTransactions() {
        List<Transaction> pendingTransactions = transactionRepository.findByStatus(TransactionStatus.PENDING);

        for (Transaction transaction : pendingTransactions) {
            Account account = transaction.getAccount();
            BigDecimal amount = BigDecimal.valueOf(transaction.getAmount());

            if (transaction.getType() == TransactionType.PURCHASE) {
                if (account.getBalance().compareTo(amount) >= 0) {
                    account.setBalance(account.getBalance().subtract(amount));
                    transaction.setStatus(TransactionStatus.SUCCESS);
                } else {
                    transaction.setStatus(TransactionStatus.FAILED);
                }
            } else if (transaction.getType() == TransactionType.TOP_UP) {
                account.setBalance(account.getBalance().add(amount));
                transaction.setStatus(TransactionStatus.SUCCESS);
            }

            accountRepository.save(account);
            transactionRepository.save(transaction);
        }

        log.info("Successfully processed " + pendingTransactions.size() + " pending transactions.");

    }}
