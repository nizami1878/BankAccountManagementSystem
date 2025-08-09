package com.matrix.BankAccountManagementSystem.mapper;

import com.matrix.BankAccountManagementSystem.dto.CustomerDto;
import com.matrix.BankAccountManagementSystem.model.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    List<CustomerDto> toDtoList(List<Customer> customers);

    CustomerDto toDto(Customer customer);
}
