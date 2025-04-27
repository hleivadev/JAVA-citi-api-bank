package com.citi.financialtransactionstaxes.apiciti.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "transactions")
@Schema(description = "Entity representing a financial transaction")
public class Transaction {

    @Id
    @Schema(description = "Unique transaction ID", example = "65a1b2c3d4e5f67890")
    private String id;

    @Field(name = "amount")
    @Schema(description = "Transaction amount", example = "100.00")
    private BigDecimal amount;

    @Field(name = "timestamp")
    @Schema(description = "Transaction timestamp", example = "2025-02-10T15:30:00")
    private LocalDateTime timestamp;

    @Field(name = "tax_amount")
    @Schema(description = "Applied tax amount", example = "10.00")
    private BigDecimal taxAmount;


    public Transaction() {}

    public Transaction(BigDecimal amount, BigDecimal taxAmount) {
        this.amount = amount;
        this.taxAmount = taxAmount;
        this.timestamp = LocalDateTime.now();
    }


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
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
