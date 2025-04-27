package com.citi.financialtransactionstaxes.apiciti.adapter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citi.financialtransactionstaxes.apiciti.application.usecase.ITransactionService;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.dto.TransactionResponseDTO;
import com.citi.financialtransactionstaxes.apiciti.domain.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transaction API", description = "Endpoints for managing financial transactions")  // Swagger Tag
public class TransactionController {

    private final ITransactionService transactionService;

    public TransactionController(ITransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create a new transaction", description = "Processes a new transaction and calculates tax")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Transaction successfully created",
                     content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                     content = @Content(schema = @Schema(implementation = String.class))),
        @ApiResponse(responseCode = "409", description = "Transaction conflict (duplicate amount)",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<TransactionResponseDTO>> createTransaction(
            @Valid @RequestBody TransactionDTO transactionDTO) {
        TransactionResponseDTO transaction = transactionService.createTransaction(transactionDTO);
        GenericResponse<TransactionResponseDTO> response =
                new GenericResponse<>("Transaction created successfully", transaction);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieves a list of all recorded transactions")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful retrieval",
                     content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "No transactions found",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<List<TransactionResponseDTO>>> getAllTransactions() {
        List<TransactionResponseDTO> transactions = transactionService.getAllTransactions();
        GenericResponse<List<TransactionResponseDTO>> response =
                new GenericResponse<>("Transaction list retrieved successfully", transactions);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Finds a specific transaction by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transaction found",
                     content = @Content(schema = @Schema(implementation = TransactionResponseDTO.class))),
        @ApiResponse(responseCode = "404", description = "Transaction not found",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<TransactionResponseDTO>> getTransactionById(@PathVariable String id) {
        TransactionResponseDTO transaction = transactionService.getTransactionById(id);
        GenericResponse<TransactionResponseDTO> response =
                new GenericResponse<>("Transaction retrieved successfully", transaction);
        return ResponseEntity.ok(response);
    }
}
