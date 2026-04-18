package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CountResponseTest {

    @Test
    @DisplayName("Should create immutable CountResponse")
    void shouldBeImmutable() {
        CountResponse.SearchDetail detail = new CountResponse.SearchDetail(
                "hotel1", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(1, 3, 29, 30));

        CountResponse response = new CountResponse("id1", detail, 100);

        assertAll(
                () -> assertEquals("id1", response.searchId()),
                () -> assertEquals(100, response.count()),
                () -> assertEquals("hotel1", response.search().hotelId()),
                () -> assertThrows(UnsupportedOperationException.class, () -> response.search().ages().add(5))
        );
    }

    @Test
    @DisplayName("SearchDetail should handle null ages")
    void shouldHandleNullAges() {
        CountResponse.SearchDetail detail = new CountResponse.SearchDetail(
                "hotel1", LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), null);

        assertAll(
                () -> assertNotNull(detail.ages()),
                () -> assertTrue(detail.ages().isEmpty())
        );
    }
}

