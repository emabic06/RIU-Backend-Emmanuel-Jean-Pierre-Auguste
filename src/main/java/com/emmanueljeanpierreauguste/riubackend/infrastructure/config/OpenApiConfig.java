package com.emmanueljeanpierreauguste.riubackend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI/Swagger configuration for API documentation.
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("RIU Backend - Hotel Availability Search API")
                        .version("1.0.0")
                        .description("API for searching hotel availability and counting identical searches")
                        .contact(new Contact()
                                .name("Emmanuel Jean Pierre Auguste")
                        )
                );
    }
}

