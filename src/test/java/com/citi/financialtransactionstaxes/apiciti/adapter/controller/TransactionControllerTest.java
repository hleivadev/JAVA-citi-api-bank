package com.citi.financialtransactionstaxes.apiciti.adapter.controller;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.citi.financialtransactionstaxes.apiciti.application.usecase.ITransactionService;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionResponseDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.response.GenericResponse;

class TransactionControllerTest {

    @Mock
    private ITransactionService transactionService;

    @InjectMocks
    private TransactionController transactionController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransaction() {
        TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(100));
        TransactionResponseDTO responseDTO = new TransactionResponseDTO("1", BigDecimal.valueOf(100), null, BigDecimal.valueOf(15));

        when(transactionService.createTransaction(any(TransactionDTO.class))).thenReturn(responseDTO);

        ResponseEntity<GenericResponse<TransactionResponseDTO>> response = transactionController.createTransaction(dto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction created successfully", response.getBody().getMessage());
        assertEquals(responseDTO, response.getBody().getData());
    }

    @Test
    void testGetAllTransactions() {
        List<TransactionResponseDTO> transactions = List.of(new TransactionResponseDTO("1", BigDecimal.valueOf(100), null, BigDecimal.valueOf(15)));
        when(transactionService.getAllTransactions()).thenReturn(transactions);

        ResponseEntity<GenericResponse<List<TransactionResponseDTO>>> response = transactionController.getAllTransactions();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction list retrieved successfully", response.getBody().getMessage());
        assertEquals(transactions, response.getBody().getData());
    }

    @Test
    void testGetTransactionById() {
        TransactionResponseDTO transaction = new TransactionResponseDTO("1", BigDecimal.valueOf(100), null, BigDecimal.valueOf(15));
        when(transactionService.getTransactionById("1")).thenReturn(transaction);

        ResponseEntity<GenericResponse<TransactionResponseDTO>> response = transactionController.getTransactionById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Transaction retrieved successfully", response.getBody().getMessage());
        assertEquals(transaction, response.getBody().getData());
    }
}
