package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest;

import com.emmanueljeanpierreauguste.riubackend.domain.exception.SearchNotFoundException;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.ErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle SearchNotFoundException with 404")
    void shouldHandleSearchNotFound() {
        SearchNotFoundException ex = new SearchNotFoundException("abc");

        ResponseEntity<ErrorResponse> response = handler.handleSearchNotFound(ex);

        assertAll(
                () -> assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(404, response.getBody().status()),
                () -> assertTrue(response.getBody().errors().getFirst().contains("abc"))
        );
    }

    @Test
    @DisplayName("Should handle generic exception with 500")
    void shouldHandleGenericException() {
        Exception ex = new RuntimeException("boom");

        ResponseEntity<ErrorResponse> response = handler.handleGenericException(ex);

        assertAll(
                () -> assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode()),
                () -> assertNotNull(response.getBody()),
                () -> assertEquals(500, response.getBody().status())
        );
    }
}

