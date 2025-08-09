package com.matrix.BankAccountManagementSystem.model.repository;

import com.matrix.BankAccountManagementSystem.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findById(Integer customerId);
}
