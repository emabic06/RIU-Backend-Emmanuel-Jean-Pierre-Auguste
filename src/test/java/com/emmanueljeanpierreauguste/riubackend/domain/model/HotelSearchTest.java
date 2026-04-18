package com.emmanueljeanpierreauguste.riubackend.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for HotelSearch domain model.
 */
class HotelSearchTest {

    @Test
    @DisplayName("Should create an immutable HotelSearch with defensive copy of ages")
    void shouldCreateImmutableHotelSearch() {
        List<Integer> ages = new ArrayList<>(List.of(30, 29, 1));
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), ages);

        ages.add(99);

        assertAll(
                () -> assertEquals("id1", search.searchId()),
                () -> assertEquals("hotel1", search.hotelId()),
                () -> assertEquals(LocalDate.of(2023, 12, 29), search.checkIn()),
                () -> assertEquals(LocalDate.of(2023, 12, 31), search.checkOut()),
                () -> assertEquals(3, search.ages().size()),
                () -> assertThrows(UnsupportedOperationException.class, () -> search.ages().add(5))
        );
    }

    @Test
    @DisplayName("Should handle null ages with empty list")
    void shouldHandleNullAges() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), null);

        assertAll(
                () -> assertNotNull(search.ages()),
                () -> assertTrue(search.ages().isEmpty())
        );
    }
}

