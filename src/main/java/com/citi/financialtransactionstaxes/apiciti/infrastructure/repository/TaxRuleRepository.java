package com.citi.financialtransactionstaxes.apiciti.infrastructure.repository;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;

public interface TaxRuleRepository extends MongoRepository<TaxRule, String> {   

    boolean existsByTaxPercentage(BigDecimal taxPercentage);
    Optional<TaxRule> findByTaxPercentageAndRuleName(BigDecimal taxPercentage, String ruleName);
}
