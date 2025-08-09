package com.matrix.BankAccountManagementSystem.service;

import com.matrix.BankAccountManagementSystem.dto.CustomerDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateCustomerRequest;
import com.matrix.BankAccountManagementSystem.mapper.CustomerMapper;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
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
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private CustomerMapper customerMapper;

    private CreateCustomerRequest request;
    private Customer customer;

    @BeforeEach
    void setup() {
        request = new CreateCustomerRequest();
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setEmail("john.doe@example.com");
        request.setPhoneNumber("1234567890");
        request.setBirthDate(LocalDate.of(1990, 1, 1));

        Account account = new Account();
        account.setId(100L);
        account.setAccountNumber("ACC100XYZ");
        account.setBalance(BigDecimal.valueOf(100));
        account.setCustomer(customer);

        customer = new Customer();
        customer.setId(1L);
        customer.setFirstName("John");
        customer.setLastName("Doe");
        customer.setEmail("john.doe@example.com");
        customer.setPhoneNumber("1234567890");
        customer.setBirthDate(LocalDate.of(1990, 1, 1));
        customer.setAccounts(new ArrayList<>(List.of(account)));

        account.setCustomer(customer);
    }


    @Test
    void testCreateCustomer_Success() {
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(accountRepository.save(any(Account.class))).thenReturn(new Account());

        customerService.createCustomer(request);

        verify(customerRepository, times(1)).save(any(Customer.class));
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void testGetAllCustomers() {
        List<Customer> customers = List.of(customer);
        List<CustomerDto> dtos = List.of(new CustomerDto());

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(new CustomerDto());

        List<CustomerDto> result = customerService.getAllCustomers();

        assertEquals(1, result.size());
        verify(customerRepository).findAll();
    }

    @Test
    void testGetById_CustomerExists() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerMapper.toDto(customer)).thenReturn(new CustomerDto());

        CustomerDto result = customerService.getById(1);

        assertNotNull(result);
        verify(customerRepository).findById(1);
    }

    @Test
    void testGetById_CustomerNotFound() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> customerService.getById(99));
    }

    @Test
    void testUpdateCustomer_Success() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(customerMapper.toDto(any(Customer.class))).thenReturn(new CustomerDto());

        CustomerDto result = customerService.updateCustomer(1, request);

        assertNull(result);
        verify(customerRepository).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(NullPointerException.class, () -> customerService.updateCustomer(1, request));
    }

    @Test
    void testDeleteById_Success() {
        when(customerRepository.existsById(1)).thenReturn(true);

        customerService.deleteById(1);

        verify(customerRepository).deleteById(1);
    }

    @Test
    void testDeleteById_CustomerNotFound() {
        when(customerRepository.existsById(1)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> customerService.deleteById(1));
    }
}
