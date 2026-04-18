package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRequestTest {

    @Test
    @DisplayName("Should create immutable SearchRequest with defensive copy")
    void shouldBeImmutable() {
        List<Integer> ages = new ArrayList<>(List.of(30, 29));
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), ages);

        ages.add(99);

        assertAll(
                () -> assertEquals(2, request.ages().size()),
                () -> assertThrows(UnsupportedOperationException.class, () -> request.ages().add(5))
        );
    }

    @Test
    @DisplayName("Should handle null ages")
    void shouldHandleNullAges() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), null);

        assertAll(
                () -> assertNotNull(request.ages()),
                () -> assertTrue(request.ages().isEmpty())
        );
    }
}

