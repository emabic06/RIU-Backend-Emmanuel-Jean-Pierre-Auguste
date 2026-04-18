package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ErrorResponseTest {

    @Test
    @DisplayName("Should create immutable ErrorResponse with defensive copy")
    void shouldBeImmutable() {
        List<String> errors = new ArrayList<>(List.of("error1", "error2"));
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 400, errors);

        errors.add("error3");

        assertAll(
                () -> assertEquals(2, response.errors().size()),
                () -> assertEquals(400, response.status()),
                () -> assertThrows(UnsupportedOperationException.class, () -> response.errors().add("x"))
        );
    }

    @Test
    @DisplayName("Should handle null errors")
    void shouldHandleNullErrors() {
        ErrorResponse response = new ErrorResponse(LocalDateTime.now(), 400, null);

        assertAll(
                () -> assertNotNull(response.errors()),
                () -> assertTrue(response.errors().isEmpty())
        );
    }
}

