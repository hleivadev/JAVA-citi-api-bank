package com.citi.financialtransactionstaxes.apiciti.application.service;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionResponseDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.Transaction;
import com.citi.financialtransactionstaxes.apiciti.exception.BadRequestException;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TaxRuleRepository;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TransactionRepository;

class TransactionUseCaseTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private TaxRuleRepository taxRuleRepository;

    @InjectMocks
    private TransactionUseCase transactionUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction_Success() {
        TransactionDTO dto = new TransactionDTO(new BigDecimal("100.00"));

        TaxRule mockTaxRule = new TaxRule("Standard Tax", new BigDecimal("15"));
        when(taxRuleRepository.findAll()).thenReturn(java.util.List.of(mockTaxRule));

        Transaction savedTransaction = new Transaction(new BigDecimal("100.00"), new BigDecimal("15.00"));
        savedTransaction.setId("1");

        when(transactionRepository.existsByAmount(dto.getAmount())).thenReturn(false);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(savedTransaction);

        TransactionResponseDTO response = transactionUseCase.createTransaction(dto);

        assertNotNull(response);
        assertEquals("1", response.getId());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
        assertNotNull(response.getTimestamp());
        assertEquals(new BigDecimal("15.00"), response.getTaxAmount());

        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_InvalidAmount_ShouldThrowBadRequestException() {
        TransactionDTO dto = new TransactionDTO(new BigDecimal("-10.00"));

        assertThrows(BadRequestException.class, () -> transactionUseCase.createTransaction(dto));
    }

    @Test
    void testCreateTransaction_Conflict_ShouldThrowConflictException() {
        TransactionDTO dto = new TransactionDTO(new BigDecimal("100.00"));
        when(transactionRepository.existsByAmount(dto.getAmount())).thenReturn(true);

        assertThrows(com.citi.financialtransactionstaxes.apiciti.exception.ConflictException.class,
                () -> transactionUseCase.createTransaction(dto));
    }
}