package com.citi.financialtransactionstaxes.apiciti.infrastructure.repository;

import java.math.BigDecimal;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    boolean existsByAmount(BigDecimal amount);
}
