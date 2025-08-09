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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {

    @InjectMocks
    private TransactionService transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionMapper transactionMapper;

    @Test
    public void testCreateTransaction_TopUp_Success() {
        String accountNumber = "ACC123";
        int amount = 100;
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);

        CreateTransactionRequest request = new CreateTransactionRequest();
        request.setAccountNumber(accountNumber);
        request.setAmount(amount);
        request.setType(TransactionType.TOP_UP);

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TOP_UP);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setAccount(account);

        TransactionDto dto = new TransactionDto();
        dto.setId(1);
        dto.setAmount(amount);
        dto.setType(TransactionType.TOP_UP);
        dto.setStatus(TransactionStatus.PENDING);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(dto);

        TransactionDto result = transactionService.createTransaction(request);

        assertEquals(TransactionStatus.PENDING, result.getStatus());
        assertEquals(TransactionType.TOP_UP, result.getType());
        verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    public void testTopUp_ShouldReturnTransactionDto() {

        String accountNumber = "ACC123";
        int amount = 50;
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.ZERO);

        Transaction transaction = new Transaction();
        transaction.setId(2L);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.TOP_UP);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setAccount(account);

        TransactionDto dto = new TransactionDto();
        dto.setId(2);
        dto.setAmount(amount);
        dto.setType(TransactionType.TOP_UP);
        dto.setStatus(TransactionStatus.PENDING);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(dto);


        TransactionDto result = transactionService.topUp(accountNumber, amount);

        assertEquals(TransactionType.TOP_UP, result.getType());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
    }

    @Test
    public void testPurchase_InsufficientBalance_ShouldThrowException() {

        String accountNumber = "ACC001";
        int amount = 100;

        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.valueOf(50));

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                transactionService.purchase(accountNumber, amount));

        assertEquals("Insufficient balance for the purchase", ex.getMessage());
    }
    @Test
    public void testFindById_Success() {
        Integer transactionId = 1;
        Transaction transaction = new Transaction();
        transaction.setId(Long.valueOf(transactionId));
        transaction.setAmount(100);
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.PENDING);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionId);
        transactionDto.setAmount(100);
        transactionDto.setType(TransactionType.PURCHASE);
        transactionDto.setStatus(TransactionStatus.PENDING);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.findById(transactionId);

        assertNotNull(result);
        assertEquals(transactionId, result.getId());
        assertEquals(100, result.getAmount());
        assertEquals(TransactionType.PURCHASE, result.getType());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
    }
    @Test
    public void testPurchase_Success() {
        String accountNumber = "ACC123";
        int amount = 100;
        Account account = new Account();
        account.setAccountNumber(accountNumber);
        account.setBalance(BigDecimal.valueOf(500));

        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(amount);
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.PENDING);
        transaction.setAccount(account);

        TransactionDto dto = new TransactionDto();
        dto.setId(1);
        dto.setAmount(amount);
        dto.setType(TransactionType.PURCHASE);
        dto.setStatus(TransactionStatus.PENDING);

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(Optional.of(account));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(any(Transaction.class))).thenReturn(dto);

        TransactionDto result = transactionService.purchase(accountNumber, amount);

        assertEquals(TransactionType.PURCHASE, result.getType());
        assertEquals(TransactionStatus.PENDING, result.getStatus());
    }
    @Test
    public void testChangePaymentStatus_Success() {
        Integer transactionId = 1;
        Transaction transaction = new Transaction();
        transaction.setId(Long.valueOf(transactionId));
        transaction.setAmount(100);
        transaction.setType(TransactionType.PURCHASE);
        transaction.setStatus(TransactionStatus.PENDING);

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setId(transactionId);
        transactionDto.setAmount(100);
        transactionDto.setType(TransactionType.PURCHASE);
        transactionDto.setStatus(TransactionStatus.SUCCESS);

        when(transactionRepository.findById(transactionId)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(transaction)).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        TransactionDto result = transactionService.changePaymentStatus(transactionId, TransactionStatus.SUCCESS);

        assertEquals(TransactionStatus.SUCCESS, result.getStatus());
        verify(transactionRepository).save(transaction);
    }
}
