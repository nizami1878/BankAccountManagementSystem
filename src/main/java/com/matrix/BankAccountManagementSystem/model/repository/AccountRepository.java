package com.matrix.BankAccountManagementSystem.model.repository;

import com.matrix.BankAccountManagementSystem.model.entity.Account;
import com.matrix.BankAccountManagementSystem.model.entity.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
  List<Account> findByCustomerIdAndStatus(Integer customerId, AccountStatus accountStatus);
  List<Account> findByCustomerId(Integer customerId);
  Optional<Account> findByAccountNumber(String accountNumber);
}
