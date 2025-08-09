package com.matrix.BankAccountManagementSystem.mapper;

import com.matrix.BankAccountManagementSystem.dto.AccountDto;
import com.matrix.BankAccountManagementSystem.dto.TransactionDto;
import com.matrix.BankAccountManagementSystem.dto.request.CreateTransactionRequest;
import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel ="spring")
public interface TransactionMapper {
    @Mapping(source = "account.accountNumber", target = "accountNumber")
    TransactionDto toDto(Transaction transaction);

    Transaction toEntity(CreateTransactionRequest request);
}
