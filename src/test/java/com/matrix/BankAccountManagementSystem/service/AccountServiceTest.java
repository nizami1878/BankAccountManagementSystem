package com.matrix.BankAccountManagementSystem.service;

import com.matrix.BankAccountManagementSystem.dto.AccountDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateAccountRequest;
import com.matrix.BankAccountManagementSystem.mapper.AccountMapper;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.AccountStatus;
import com.matrix.BankAccountManagementSystem.model.entity.Customer;
import com.matrix.BankAccountManagementSystem.model.repository.AccountRepository;
import com.matrix.BankAccountManagementSystem.model.repository.CustomerRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @InjectMocks
    private AccountService accountService;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountMapper accountMapper;

    private Customer customer;
    private Account account;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        customer.setId(1L);
        account = new Account();
        account.setId(1L);
        account.setAccountNumber("ACC001");
        account.setBalance(BigDecimal.valueOf(100));
        account.setCustomer(customer);
    }

    @Test
    public void testCreateAccount_Success() {
        CreateAccountRequest request = new CreateAccountRequest();
        request.setBalance(BigDecimal.valueOf(200));

        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);

        accountService.createAccount(1, request);

        verify(accountRepository).save(any(Account.class));
    }

    @Test
    public void testCreateAccount_CustomerNotFound_ShouldThrowException() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());

        CreateAccountRequest request = new CreateAccountRequest();

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                accountService.createAccount(1, request));

        assertEquals("Customer not found", ex.getMessage());
    }

    @Test
    public void testGetAllAccounts() {
        when(accountRepository.findAll()).thenReturn(List.of(account));
        when(accountMapper.toDtoList(anyList())).thenReturn(List.of(new AccountDto()));

        List<AccountDto> result = accountService.getAllAccounts();

        assertEquals(1, result.size());
    }

    @Test
    public void testGetById_AccountExists() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));
        when(accountMapper.toDto(account)).thenReturn(new AccountDto());

        AccountDto result = accountService.getById(1);

        assertNotNull(result);
    }

    @Test
    public void testDeleteById_Success() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        accountService.deleteById(1);

        verify(accountRepository).delete(account);
    }

    @Test
    public void testChangeAccountStatus_Success() {
        when(accountRepository.findById(1)).thenReturn(Optional.of(account));

        accountService.changeAccountStatus(1, AccountStatus.Blocked);

        assertEquals(AccountStatus.Blocked, account.getStatus());
        verify(accountRepository).save(account);
    }

    @Test
    public void testGetActiveAccountsByCustomerId() {
        account.setStatus(AccountStatus.Active);
        when(accountRepository.findByCustomerIdAndStatus(1, AccountStatus.Active)).thenReturn(List.of(account));

        List<AccountDto> result = accountService.getActiveAccountsByCustomerId(1);

        assertEquals(1, result.size());
    }

    @Test
    public void testIncreaseAccountBalance() {
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(account));

        accountService.increaseAccountBalance("ACC001", BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), account.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    public void testDecreaseAccountBalance() {
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(Optional.of(account));

        accountService.decreaseAccountBalance("ACC001", BigDecimal.valueOf(40));

        assertEquals(BigDecimal.valueOf(60), account.getBalance());
        verify(accountRepository).save(account);
    }

    @Test
    public void testGetAccountByNumber_NotFound_ShouldThrowException() {
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                accountService.increaseAccountBalance("INVALID", BigDecimal.valueOf(10)));

        assertEquals("Account not found", ex.getMessage());
    }
}

