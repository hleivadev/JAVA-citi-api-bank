package com.citi.financialtransactionstaxes.apiciti.adapter.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citi.financialtransactionstaxes.apiciti.application.service.TaxRuleService;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.domain.response.GenericResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/tax-rules")
@Tag(name = "Tax Rule API", description = "Endpoints for managing tax rules")  // Swagger Tag
public class TaxRuleController {

    private final TaxRuleService taxRuleService;

    public TaxRuleController(TaxRuleService taxRuleService) {
        this.taxRuleService = taxRuleService;
    }

    @PostMapping
    @Operation(summary = "Create a new tax rule", description = "Adds a new tax rule to the system")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Tax rule created successfully",
                content = @Content(schema = @Schema(implementation = TaxRule.class))),
        @ApiResponse(responseCode = "400", description = "Invalid request",
                content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<TaxRule>> createTaxRule(@Valid @RequestBody TaxRule taxRule) {
        TaxRule savedTaxRule = taxRuleService.createTaxRule(taxRule);
        GenericResponse<TaxRule> response = new GenericResponse<>("Tax rule created successfully", savedTaxRule);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all tax rules", description = "Retrieves a list of all tax rules")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Successful retrieval",
                     content = @Content(schema = @Schema(implementation = TaxRule.class))),
        @ApiResponse(responseCode = "404", description = "No tax rules found",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<List<TaxRule>>> getAllTaxRules() {
        List<TaxRule> taxRules = taxRuleService.getAllTaxRules();
        GenericResponse<List<TaxRule>> response = new GenericResponse<>("Tax rules retrieved successfully", taxRules);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tax rule by ID", description = "Finds a specific tax rule by its ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Tax rule found",
                     content = @Content(schema = @Schema(implementation = TaxRule.class))),
        @ApiResponse(responseCode = "404", description = "Tax rule not found",
                     content = @Content(schema = @Schema(implementation = String.class)))
    })
    public ResponseEntity<GenericResponse<TaxRule>> getTaxRuleById(@PathVariable String id) {
        TaxRule taxRule = taxRuleService.getTaxRuleById(id);
        GenericResponse<TaxRule> response = new GenericResponse<>("Tax rule retrieved successfully", taxRule);
        return ResponseEntity.ok(response);
    }
}
