package com.matrix.BankAccountManagementSystem.mapper;

import com.matrix.BankAccountManagementSystem.dto.AccountDto;
import com.matrix.BankAccountManagementSystem.dto.CustomerDto;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.Customer;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    List<AccountDto> toDtoList(List<Account> accounts);
    AccountDto toDto(Account account);
}
