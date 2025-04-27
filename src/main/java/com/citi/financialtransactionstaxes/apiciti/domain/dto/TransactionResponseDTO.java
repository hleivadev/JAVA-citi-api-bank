package com.citi.financialtransactionstaxes.apiciti.domain.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO representing the response of a transaction")
public class TransactionResponseDTO {

    @JsonProperty("transaction_id")
    @Schema(description = "Unique transaction ID", example = "65a1b2c3d4e5f67890")
    private final String id;

    @Schema(description = "Transaction amount", example = "100.00")
    private final BigDecimal amount;

    @Schema(description = "Transaction timestamp", example = "2025-02-10T15:30:00")
    private final LocalDateTime timestamp;

    @Schema(description = "Applied tax amount", example = "10.00")
    private final BigDecimal taxAmount;

    public TransactionResponseDTO(String id, BigDecimal amount, LocalDateTime timestamp, BigDecimal taxAmount) {
        this.id = id;
        this.amount = amount;
        this.timestamp = timestamp;
        this.taxAmount = taxAmount;
    }

    public String getId() {
        return id;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }    
}
