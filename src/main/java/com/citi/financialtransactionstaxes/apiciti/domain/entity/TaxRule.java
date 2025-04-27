package com.citi.financialtransactionstaxes.apiciti.domain.entity;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import io.swagger.v3.oas.annotations.media.Schema;

@Document(collection = "tax_rules")
public class TaxRule {

    @Id  
    @Schema(description = "ID único de la regla de impuestos", example = "65a1b2c3d4e5f67890")
    private String id;

    @Field(name = "rule_name")  
    @Schema(description = "Nombre de la regla de impuestos", example = "IVA General")
    private String ruleName;

    @Field(name = "tax_percentage")  
    @Schema(description = "Porcentaje del impuesto aplicado", example = "15.00")
    private BigDecimal taxPercentage;
    
    //constructor
    public TaxRule(String ruleName, BigDecimal taxPercentage) {
        this.ruleName = ruleName;
        this.taxPercentage = taxPercentage;
    }

    //getters
    public String getId() { return id; }
    public String getRuleName() { return ruleName; }
    public BigDecimal getTaxPercentage() { return taxPercentage; }
}