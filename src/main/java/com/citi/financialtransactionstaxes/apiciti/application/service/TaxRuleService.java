package com.citi.financialtransactionstaxes.apiciti.application.service;

import java.util.List;
import java.util.Optional;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;
import com.citi.financialtransactionstaxes.apiciti.exception.BadRequestException;
import com.citi.financialtransactionstaxes.apiciti.exception.ConflictException;
import com.citi.financialtransactionstaxes.apiciti.exception.ResourceNotFoundException;
import com.citi.financialtransactionstaxes.apiciti.infrastructure.repository.TaxRuleRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaxRuleService {

    private final TaxRuleRepository taxRuleRepository;

    public TaxRuleService(TaxRuleRepository taxRuleRepository) {
        this.taxRuleRepository = taxRuleRepository;
    }

    /**
      * Creates a new tax rule and saves it to the database.
      */
    public TaxRule createTaxRule(TaxRule taxRule) {
        try {
            log.info("createTaxRule a new tax rule and saves it to the database");
    
            Optional<TaxRule> existingTaxRule = taxRuleRepository.findByTaxPercentageAndRuleName(
                    taxRule.getTaxPercentage(), taxRule.getRuleName());
            //validation if is present
            if (existingTaxRule.isPresent()) {
                TaxRule existing = existingTaxRule.get();
                log.warn("Conflict: A tax rule with the same percentage {} and name '{}' already exists.",
                        existing.getTaxPercentage(), existing.getRuleName());
    
                throw new ConflictException("A tax rule with the same percentage and name already exists.", existing);
            }
    
            log.info("Saving new tax rule: {}", taxRule);
            return taxRuleRepository.save(taxRule);
    
        } catch (DataAccessException e) {
            log.error("Database error while saving tax rule: {}", e.getMessage(), e);
            throw new BadRequestException("Error saving tax rule. Please check the input data.");
        }
    }
    
    /**
     * Retrieves all tax rules.
     */
    public List<TaxRule> getAllTaxRules() {
        try {
            log.info("getAllTaxRules Fetching all tax rules");
            List<TaxRule> taxRules = taxRuleRepository.findAll();

            if (taxRules.isEmpty()) {
                log.warn("No tax rules found in the database.");
                throw new ResourceNotFoundException("No tax rules found.");
            }

            return taxRules;
        } catch (ResourceNotFoundException e) {
            log.error("Error retrieving tax rules: {}", e.getMessage(), e);
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while retrieving tax rules: {}", e.getMessage(), e);
            throw new BadRequestException("Database error occurred while fetching tax rules.");
        } catch (Exception e) {
            log.error("Unexpected error while retrieving tax rules: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while retrieving tax rules.");
        }
    }

    /**
     * Retrieves a tax rule by ID.
     */
    public TaxRule getTaxRuleById(String id) {
        try {
            log.info("getTaxRuleById Fetching tax rule with ID: {}", id);

            if (id == null || id.trim().isEmpty()) {
                log.warn("Invalid tax rule ID provided: {}", id);
                throw new BadRequestException("Tax rule ID cannot be null or empty.");
            }

            return taxRuleRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("TaxRule", "id", id));

        } catch (ResourceNotFoundException e) {
            log.error("Tax rule not found: {}", e.getMessage());
            throw e;
        } catch (DataAccessException e) {
            log.error("Database error while retrieving tax rule: {}", e.getMessage(), e);
            throw new BadRequestException("Database error occurred while fetching the tax rule.");
        } catch (Exception e) {
            log.error("Unexpected error while retrieving tax rule: {}", e.getMessage(), e);
            throw new RuntimeException("Unexpected error while retrieving tax rule.");
        }
    }

}
