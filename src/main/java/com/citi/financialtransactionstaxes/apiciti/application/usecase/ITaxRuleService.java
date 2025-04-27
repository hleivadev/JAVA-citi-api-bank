package com.citi.financialtransactionstaxes.apiciti.application.usecase;

import java.util.List;
import java.util.Optional;

import com.citi.financialtransactionstaxes.apiciti.domain.entity.TaxRule;

/** Application Layer */
public interface ITaxRuleService {

    TaxRule createTaxRule(TaxRule taxRule);
    
    List<TaxRule> getAllTaxRules();
    
    Optional<TaxRule> getTaxRuleById(String id);
}
