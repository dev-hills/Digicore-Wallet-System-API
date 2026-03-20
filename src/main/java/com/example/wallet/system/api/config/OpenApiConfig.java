package com.example.wallet.system.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI walletOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Wallet Management API")
                        .description("RESTful API for creating wallets, funding them, and performing debit transactions. " +
                                "All monetary values use BigDecimal to avoid floating-point precision issues.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Wallet API Support")
                                .email("support@walletapi.com")));
    }
}
