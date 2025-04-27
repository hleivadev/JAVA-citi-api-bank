package com.citi.financialtransactionstaxes.apiciti.adapter.repository;

import java.math.BigDecimal;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.Transaction;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    boolean existsByAmount(BigDecimal amount);
}
