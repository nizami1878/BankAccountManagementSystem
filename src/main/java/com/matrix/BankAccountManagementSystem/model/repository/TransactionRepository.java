package com.matrix.BankAccountManagementSystem.model.repository;

import com.matrix.BankAccountManagementSystem.model.entity.Transaction;
import com.matrix.BankAccountManagementSystem.model.entity.TransactionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByStatus(TransactionStatus status);
}
