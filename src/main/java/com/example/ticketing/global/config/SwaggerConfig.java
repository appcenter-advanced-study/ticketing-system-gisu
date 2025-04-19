package com.example.ticketing.global.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    private Info apiInfo() {
        return new Info()
                .title("Ticketing API")
                .description("Ticketing API 설명서")
                .version("1.0.0");
    }
}