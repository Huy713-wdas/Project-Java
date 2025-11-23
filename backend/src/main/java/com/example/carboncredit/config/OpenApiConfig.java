package com.example.carboncredit.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.customizers.OpenApiCustomizer;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
            .info(new Info().title("Carbon Credit Admin API")
            .version("1.0")
            .description("Admin endpoints for Carbon Credit Marketplace"));
    }
}
