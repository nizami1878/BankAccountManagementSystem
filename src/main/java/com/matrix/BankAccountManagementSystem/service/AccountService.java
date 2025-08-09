package com.matrix.BankAccountManagementSystem.service;

import com.matrix.BankAccountManagementSystem.dto.AccountDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateAccountRequest;
import com.matrix.BankAccountManagementSystem.mapper.AccountMapper;
import com.matrix.BankAccountManagementSystem.model.entity.*;
import com.matrix.BankAccountManagementSystem.model.repository.AccountRepository;
import com.matrix.BankAccountManagementSystem.model.repository.CustomerRepository;
import com.matrix.BankAccountManagementSystem.model.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final CustomerRepository customerRepository;
    public void createAccount(Integer customer_id, CreateAccountRequest request) {
        Customer customer = customerRepository.findById(customer_id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Account account = Account.builder()
                .accountNumber(generateAccountNumber())
                .balance(request.getBalance())
                .customer(customer)
                .build();

        accountRepository.save(account);
     }
    public List<AccountDto> getAllAccounts() {
        return accountMapper.toDtoList(accountRepository.findAll());
    }

    public AccountDto getById(Integer id) {
        return accountMapper.toDto(accountRepository.findById(id)
                .orElseThrow(NullPointerException::new));
    }

    public void deleteById(Integer id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Account with ID " + id + " not found"));
        accountRepository.delete(account);
    }

    public void changeAccountStatus(Integer id, AccountStatus status) {
        accountRepository.findById(id).ifPresentOrElse(account -> {
            account.setStatus(status);
            accountRepository.save(account);
        }, () -> {
            throw new NullPointerException();
        });
    }
    public List<AccountDto> getActiveAccountsByCustomerId(Integer customer_id) {
        return accountRepository.findByCustomerIdAndStatus(customer_id, AccountStatus.Active)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    public List<AccountDto> getAccountsByCustomerId(Integer customer_id) {
        return accountRepository.findByCustomerId(customer_id)
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
    @Transactional
    public void increaseAccountBalance(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
    }
   @Transactional
    public void decreaseAccountBalance(String accountNumber, BigDecimal amount) {
        Account account = getAccountByNumber(accountNumber);
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
    }

    private Account getAccountByNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }
    private String generateAccountNumber() {
        return "ACC" + new Random().nextInt(1000000);
    }

   private AccountDto toDto(Account account) {
        AccountDto dto = new AccountDto();
        dto.setId(Math.toIntExact(account.getId()));
        dto.setAccountNumber(account.getAccountNumber());
        dto.setBalance(account.getBalance());
        dto.setCustomer_id(Math.toIntExact(account.getCustomer().getId()));
        return dto;
    }



}