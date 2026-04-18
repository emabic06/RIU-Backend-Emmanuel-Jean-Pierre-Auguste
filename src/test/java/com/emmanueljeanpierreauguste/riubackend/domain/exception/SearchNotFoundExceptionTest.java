package com.emmanueljeanpierreauguste.riubackend.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SearchNotFoundExceptionTest {

    @Test
    @DisplayName("Should contain search ID in message")
    void shouldContainSearchIdInMessage() {
        SearchNotFoundException ex = new SearchNotFoundException("abc123");

        assertAll(
                () -> assertNotNull(ex.getMessage()),
                () -> assertTrue(ex.getMessage().contains("abc123"))
        );
    }
}

