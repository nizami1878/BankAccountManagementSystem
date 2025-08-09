package com.matrix.BankAccountManagementSystem.controller;

import com.matrix.BankAccountManagementSystem.dto.AccountDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateAccountRequest;
import com.matrix.BankAccountManagementSystem.model.entity.AccountStatus;
import com.matrix.BankAccountManagementSystem.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;
    @GetMapping
    public List<AccountDto> getAll() {
        return accountService.getAllAccounts();
    }
    @PostMapping("/{customer_id}")
    @ResponseStatus(HttpStatus.CREATED)
    public void createAccount(@PathVariable Integer customer_id,
                              @RequestBody CreateAccountRequest createAccountRequest) {
        accountService.createAccount(customer_id,createAccountRequest);
    }
    @GetMapping("/{id}")
    public AccountDto getById(@PathVariable Integer id){
        return accountService.getById(id);
    }
    @DeleteMapping("/{id}")
    public void deleteAccount(@PathVariable Integer id){
        accountService.deleteById(id);
    }
    @PutMapping("/{id}")
    public void changeAccountStatus(@PathVariable Integer id, AccountStatus accountStatus){
        accountService.changeAccountStatus(id, accountStatus);
    }
    @GetMapping("/active/{customer_id}")
    public List<AccountDto> getActiveAccountsByCustomerId(@PathVariable Integer customer_id) {
        return accountService.getActiveAccountsByCustomerId(customer_id);
    }
    @GetMapping("/admin/{customer_id}")
    public List<AccountDto> getAccountsByCustomerId(@PathVariable Integer customer_id) {
        return accountService.getAccountsByCustomerId(customer_id);
    }

    @PutMapping("/increase/{accountNumber}")
   public void increaseAccountBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        accountService.increaseAccountBalance(accountNumber, amount);
    }

    @PutMapping("/decrease/{accountNumber}")
    public void decreaseAccountBalance(@PathVariable String accountNumber, @RequestParam BigDecimal amount) {
        accountService.decreaseAccountBalance(accountNumber, amount);
    }
}
