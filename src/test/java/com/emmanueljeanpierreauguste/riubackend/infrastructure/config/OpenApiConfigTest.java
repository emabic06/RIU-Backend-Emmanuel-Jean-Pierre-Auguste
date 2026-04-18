package com.emmanueljeanpierreauguste.riubackend.infrastructure.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenApiConfigTest {

    @Test
    @DisplayName("Should create OpenAPI bean with correct info")
    void shouldCreateOpenApiBean() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        assertAll(
                () -> assertNotNull(openAPI),
                () -> assertNotNull(openAPI.getInfo()),
                () -> assertEquals("RIU Backend - Hotel Availability Search API", openAPI.getInfo().getTitle()),
                () -> assertEquals("1.0.0", openAPI.getInfo().getVersion())
        );
    }
}

