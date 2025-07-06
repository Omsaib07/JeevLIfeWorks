package com.example.sms.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi studentOpenApi() {
        return GroupedOpenApi.builder()
                .group("students")
                .packagesToScan("com.example.sms.controller")
                .build();
    }
}