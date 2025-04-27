package com.citi.financialtransactionstaxes.apiciti.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Financial Transactions API citi test")
                        .version("1.0")
                        .description("API for processing financial transactions and tax calculation"));
    }
}
