package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.validator;

import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.SearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRequestValidatorTest {

    private final SearchRequestValidator validator = new SearchRequestValidator();

    @Test
    @DisplayName("Should return empty errors for valid request")
    void shouldReturnEmptyForValid() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3));

        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should return error when checkIn is after checkOut")
    void shouldReturnErrorForInvalidDates() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 31), LocalDate.of(2023, 12, 29),
                List.of(30));

        List<String> errors = validator.validate(request);
        assertAll(
                () -> assertEquals(1, errors.size()),
                () -> assertTrue(errors.getFirst().contains("checkIn must be before checkOut"))
        );
    }

    @Test
    @DisplayName("Should return error when checkIn equals checkOut")
    void shouldReturnErrorWhenSameDate() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 29),
                List.of(30));

        List<String> errors = validator.validate(request);
        assertFalse(errors.isEmpty());
    }

    @Test
    @DisplayName("Should return error for negative ages")
    void shouldReturnErrorForNegativeAges() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(-1, 30, -5));

        List<String> errors = validator.validate(request);
        assertEquals(2, errors.size());
    }

    @Test
    @DisplayName("Should handle both dates null gracefully")
    void shouldHandleBothDatesNull() {
        SearchRequest request = new SearchRequest("hotel1", null, null, List.of(30));
        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should handle only checkIn null gracefully")
    void shouldHandleCheckInNull() {
        SearchRequest request = new SearchRequest("hotel1", null,
                LocalDate.of(2023, 12, 31), List.of(30));
        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should handle only checkOut null gracefully")
    void shouldHandleCheckOutNull() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), null, List.of(30));
        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should handle null ages list gracefully")
    void shouldHandleNullAges() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                null);

        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }

    @Test
    @DisplayName("Should allow age 0")
    void shouldAllowAgeZero() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(0));

        List<String> errors = validator.validate(request);
        assertTrue(errors.isEmpty());
    }
}


