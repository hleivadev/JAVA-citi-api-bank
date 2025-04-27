package com.citi.financialtransactionstaxes.apiciti.infrastructure.config;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.citi.financialtransactionstaxes.apiciti.adapter.repository.TransactionRepository;
import com.citi.financialtransactionstaxes.apiciti.domain.entity.Transaction;

@Configuration
public class DatabaseSeeder {

    @Bean
    @SuppressWarnings("unused")
    CommandLineRunner initDatabase(TransactionRepository transactionRepository) {
        return args -> {
            if (transactionRepository.count() == 0) {
                transactionRepository.saveAll(List.of(
                    new Transaction(BigDecimal.valueOf(10000),BigDecimal.valueOf(100)),
                    new Transaction(BigDecimal.valueOf(20000),BigDecimal.valueOf(200)),
                    new Transaction(BigDecimal.valueOf(30000),BigDecimal.valueOf(300))
                ));
                System.out.println("📌 MongoDB test data.");
            }
        };
    }
}