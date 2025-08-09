package com.matrix.BankAccountManagementSystem.service;

import com.matrix.BankAccountManagementSystem.dto.CustomerDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateCustomerRequest;
import com.matrix.BankAccountManagementSystem.mapper.CustomerMapper;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.Customer;
import com.matrix.BankAccountManagementSystem.model.repository.AccountRepository;
import com.matrix.BankAccountManagementSystem.model.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AccountRepository accountRepository;

    public void createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();
        customer.setFirstName(createCustomerRequest.getFirstName());
        customer.setLastName(createCustomerRequest.getLastName());
        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
        customer.setBirthDate(createCustomerRequest.getBirthDate());

        Account account = new Account();
        account.setAccountNumber(UUID.randomUUID().toString());
        account.setBalance(BigDecimal.valueOf(100));
        account.setCustomer(customer);

        customer.getAccounts().add(account);

        customerRepository.save(customer);
        accountRepository.save(account);

    }
    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll().stream().map(customerMapper::toDto).collect(Collectors.toList());
    }
    public CustomerDto getById(Integer id) {
        return customerMapper.toDto(customerRepository.findById(id).orElseThrow(NullPointerException::new));
    }

    public CustomerDto updateCustomer(Integer id, CreateCustomerRequest createCustomerRequest) {
        customerMapper.toDto(customerRepository.findById(id)
                .map(customer -> {
                    customer.setFirstName(createCustomerRequest.getFirstName());
                    customer.setLastName(createCustomerRequest.getLastName());
                    customer.setEmail(createCustomerRequest.getEmail());
                    customer.setPhoneNumber(createCustomerRequest.getPhoneNumber());
                    customer.setBirthDate(createCustomerRequest.getBirthDate());
                    return customerRepository.save(customer);
                })
                .orElseThrow(NullPointerException::new));
        return null;
    }


    public void deleteById(Integer id) {
        if (!customerRepository.existsById(id)) {
            throw new RuntimeException("Customer not found");
        }
        customerRepository.deleteById(id);
    }
}
