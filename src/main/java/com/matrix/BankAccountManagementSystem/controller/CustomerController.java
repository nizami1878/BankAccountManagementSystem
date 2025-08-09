package com.matrix.BankAccountManagementSystem.controller;

import com.matrix.BankAccountManagementSystem.dto.CustomerDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateCustomerRequest;
import com.matrix.BankAccountManagementSystem.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;
    @GetMapping
    public List<CustomerDto> getAll() {
        return customerService.getAllCustomers();
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
         customerService.createCustomer(createCustomerRequest);
    }
    @GetMapping("/{id}")
    public CustomerDto getById(@PathVariable Integer id){
        return customerService.getById(id);
    }
    @PutMapping("/{id}")
    public CustomerDto update(@PathVariable Integer id,
                              @RequestBody CreateCustomerRequest createCustomerRequest) {
        return customerService.updateCustomer(id,createCustomerRequest);
    }
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id){
        customerService.deleteById(id);
    }


}
