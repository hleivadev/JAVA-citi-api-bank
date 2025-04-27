package com.citi.financialtransactionstaxes.apiciti.domain.dto;

import java.math.BigDecimal;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO representing a transaction request")
public class TransactionDTO {
    
    @NotNull
    @Min(value = 0, message = "Amount must be greater than zero")
    @Schema(description = "Transaction amount", example = "100.00", required = true)
    private BigDecimal amount;

    public TransactionDTO() {}

    public TransactionDTO(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
}
