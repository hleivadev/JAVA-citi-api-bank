package com.citi.financialtransactionstaxes.apiciti.application.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.exception.BadRequestException;
import com.citi.financialtransactionstaxes.apiciti.exception.ConflictException;
import com.citi.financialtransactionstaxes.apiciti.exception.ResourceNotFoundException;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TaxRuleRepository;

class TaxRuleServiceTest {

    @Mock
    private TaxRuleRepository taxRuleRepository;

    @InjectMocks
    private TaxRuleService taxRuleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTaxRule_Success() {
        TaxRule taxRule = new TaxRule("Standard Tax", BigDecimal.valueOf(15));

        when(taxRuleRepository.findByTaxPercentageAndRuleName(any(), any())).thenReturn(Optional.empty());
        when(taxRuleRepository.save(any(TaxRule.class))).thenReturn(taxRule);

        TaxRule result = taxRuleService.createTaxRule(taxRule);

        assertNotNull(result);
        assertEquals("Standard Tax", result.getRuleName());
        assertEquals(BigDecimal.valueOf(15), result.getTaxPercentage());
    }

    @Test
    void testCreateTaxRule_ConflictException() {
        TaxRule taxRule = new TaxRule("Standard Tax", BigDecimal.valueOf(15));

        when(taxRuleRepository.findByTaxPercentageAndRuleName(any(), any())).thenReturn(Optional.of(taxRule));

        ConflictException exception = assertThrows(ConflictException.class, () -> {
            taxRuleService.createTaxRule(taxRule);
        });

        assertTrue(exception.getMessage().contains("A tax rule with the same percentage and name already exists."));
    }

    @Test
    void testCreateTaxRule_BadRequestException() {
        when(taxRuleRepository.save(any(TaxRule.class))).thenThrow(new BadRequestException("Database error"));

        BadRequestException exception = assertThrows(BadRequestException.class, () -> {
            taxRuleService.createTaxRule(new TaxRule("Invalid Tax", BigDecimal.ZERO));
        });

        assertTrue(exception.getMessage().contains("Database error"));
    }

    @Test
    void testGetAllTaxRules_Success() {
        List<TaxRule> taxRules = List.of(new TaxRule("VAT", BigDecimal.valueOf(20)));

        when(taxRuleRepository.findAll()).thenReturn(taxRules);

        List<TaxRule> result = taxRuleService.getAllTaxRules();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("VAT", result.get(0).getRuleName());
    }

    @Test
    void testGetAllTaxRules_ResourceNotFoundException() {
        when(taxRuleRepository.findAll()).thenReturn(List.of());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taxRuleService.getAllTaxRules();
        });

        assertTrue(exception.getMessage().contains("No tax rules found."));
    }

    @Test
    void testGetTaxRuleById_Success() {
        TaxRule taxRule = new TaxRule("Sales Tax", BigDecimal.valueOf(10));
        when(taxRuleRepository.findById("1")).thenReturn(Optional.of(taxRule));

        TaxRule result = taxRuleService.getTaxRuleById("1");

        assertNotNull(result);
        assertEquals("Sales Tax", result.getRuleName());
    }

    @Test
    void testGetTaxRuleById_ResourceNotFoundException() {
        when(taxRuleRepository.findById("1")).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            taxRuleService.getTaxRuleById("1");
        });

        assertTrue(exception.getMessage().contains("TaxRule"));
    }
}
