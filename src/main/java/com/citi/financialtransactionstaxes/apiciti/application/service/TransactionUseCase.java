package com.citi.financialtransactionstaxes.apiciti.application.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.citi.financialtransactionstaxes.apiciti.application.usecase.ITransactionService;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionResponseDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.Transaction;
import com.citi.financialtransactionstaxes.apiciti.exception.BadRequestException;
import com.citi.financialtransactionstaxes.apiciti.exception.ConflictException;
import com.citi.financialtransactionstaxes.apiciti.exception.ResourceNotFoundException;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TaxRuleRepository;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TransactionRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TransactionUseCase implements ITransactionService {

    private final TransactionRepository transactionRepository;
    private final TaxRuleRepository taxRuleRepository;

    public TransactionUseCase(TransactionRepository transactionRepository, TaxRuleRepository taxRuleRepository) {
        this.transactionRepository = transactionRepository;
        this.taxRuleRepository = taxRuleRepository;
    }

    /** create transaction */
    @Override
    public TransactionResponseDTO createTransaction(TransactionDTO dto) {
        try {
            log.info("createTransaction with TransactionDTO ");

            if (dto == null) {
                throw new BadRequestException("Invalid transaction request");
            }

            if (dto.getAmount() == null || dto.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                log.warn("Invalid transaction amount: {}", dto.getAmount());
                throw new BadRequestException("Transaction amount must be greater than zero.");
            }

            // Check if a transaction with the same amount already exists
            if (transactionRepository.existsByAmount(dto.getAmount())) {
                log.warn("Transaction conflict detected with amount: {}", dto.getAmount());
                throw new ConflictException("A transaction with the same amount already exists.");
            }
            //call calculateTax()
            BigDecimal taxAmount = calculateTax(dto.getAmount());
            Transaction transaction = new Transaction(dto.getAmount(), taxAmount);
            //save transaction
            Transaction savedTransaction = transactionRepository.save(transaction);

            log.info("Transaction created successfully: {}", savedTransaction.getId());

            return new TransactionResponseDTO(
                    savedTransaction.getId(),
                    savedTransaction.getAmount(),
                    LocalDateTime.now(),
                    savedTransaction.getTaxAmount());

        } catch (DataAccessException e) {
            log.error("Database error: {}", e.getMessage(), e);
            throw new BadRequestException("Database error occurred while saving the transaction");
        } catch (BadRequestException | ConflictException e) {
            log.error("Business logic error: {}", e.getMessage(), e);
            throw e;
        } 
        
    }

    /** get all transactions */
    @Override
    public List<TransactionResponseDTO> getAllTransactions() {
        try {
            log.info("getAllTransactions method find all data ");
            List<Transaction> transactions = transactionRepository.findAll();

            //validation is empty
            if (transactions.isEmpty()) {
                log.warn("No transactions found in the database.");
                throw new ResourceNotFoundException("No transactions found.");
            }

            return transactions.stream()
                    .map(transaction -> new TransactionResponseDTO(
                    transaction.getId(),
                    transaction.getAmount(),
                    transaction.getTimestamp(),
                    transaction.getTaxAmount()))
                    .collect(Collectors.toList());

        } catch (ResourceNotFoundException e) {
            log.error("Error retrieving transactions: {}", e.getMessage(), e);
            throw e;
        }
    }

    /** Get Transaction By Id */
    @Override
    public TransactionResponseDTO getTransactionById(String id) {
        try {
            log.info("getTransactionById method get by id ");

            return transactionRepository.findById(id)
                .map(transaction -> {
                    log.info("Transaction found with ID: {}", id);
                    return new TransactionResponseDTO(
                            transaction.getId(),
                            transaction.getAmount(),
                            transaction.getTimestamp(),
                            transaction.getTaxAmount()
                    );
                })
                .orElseThrow(() -> {
                    log.warn("Transaction not found with ID: {}", id);
                    return new ResourceNotFoundException("Transaction", "id", id);
                });

        } catch (Exception e) {
            log.error("Error retrieving transaction by ID {}: {}", id, e.getMessage(), e);
            throw new RuntimeException("Unexpected error occurred while retrieving transaction", e);
        }
    }

    /** method to calculate tax  */
    private BigDecimal calculateTax(BigDecimal amount) {
        try {
            log.info("calculateTax method to calcule the taxes ");

            Optional<TaxRule> taxRule = taxRuleRepository
                .findAll(Sort.by(Sort.Direction.DESC, "taxPercentage"))
                .stream()
                .findFirst();

            return taxRule.map(rule -> amount.multiply(rule.getTaxPercentage().divide(BigDecimal.valueOf(100))))
                    .orElse(BigDecimal.ZERO);
        } catch (Exception e) {
            log.error("Error calculating tax: {}", e.getMessage(), e);
            throw new RuntimeException("Error calculating tax", e);
        }
    }
}
