package com.citi.financialtransactionstaxes.apiciti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.citi.financialtransactionstaxes.apiciti")
public class ApiCitiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiCitiApplication.class, args);
	}

}
