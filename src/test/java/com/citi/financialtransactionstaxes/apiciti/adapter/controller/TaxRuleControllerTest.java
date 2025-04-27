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

import com.citi.financialtransactionstaxes.apiciti.application.service.TaxRuleService;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.domain.response.GenericResponse;

class TaxRuleControllerTest {

    @Mock
    private TaxRuleService taxRuleService;

    @InjectMocks
    private TaxRuleController taxRuleController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaxRule() {
        TaxRule taxRule = new TaxRule("Standard Tax", BigDecimal.valueOf(15));
        when(taxRuleService.createTaxRule(any(TaxRule.class))).thenReturn(taxRule);

        ResponseEntity<GenericResponse<TaxRule>> response = taxRuleController.createTaxRule(taxRule);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tax rule created successfully", response.getBody().getMessage());
        assertEquals(taxRule, response.getBody().getData());
    }

    @Test
    void testGetAllTaxRules() {
        List<TaxRule> taxRules = List.of(new TaxRule("VAT", BigDecimal.valueOf(20)));
        when(taxRuleService.getAllTaxRules()).thenReturn(taxRules);

        ResponseEntity<GenericResponse<List<TaxRule>>> response = taxRuleController.getAllTaxRules();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tax rules retrieved successfully", response.getBody().getMessage());
        assertEquals(taxRules, response.getBody().getData());
    }

    @Test
    void testGetTaxRuleById() {
        TaxRule taxRule = new TaxRule("Sales Tax", BigDecimal.valueOf(10));
        when(taxRuleService.getTaxRuleById("1")).thenReturn(taxRule);

        ResponseEntity<GenericResponse<TaxRule>> response = taxRuleController.getTaxRuleById("1");

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tax rule retrieved successfully", response.getBody().getMessage());
        assertEquals(taxRule, response.getBody().getData());
    }
}
