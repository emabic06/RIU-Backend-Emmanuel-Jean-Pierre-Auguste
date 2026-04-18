package com.emmanueljeanpierreauguste.riubackend.domain.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for SearchCount domain model.
 */
class SearchCountTest {

    @Test
    @DisplayName("Should create immutable SearchCount")
    void shouldCreateImmutableSearchCount() {
        SearchCount count = new SearchCount("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(1, 3, 29, 30), 100);

        assertAll(
                () -> assertEquals("id1", count.searchId()),
                () -> assertEquals("hotel1", count.hotelId()),
                () -> assertEquals(100, count.count()),
                () -> assertEquals(4, count.ages().size()),
                () -> assertThrows(UnsupportedOperationException.class, () -> count.ages().add(5))
        );
    }

    @Test
    @DisplayName("Should handle null ages")
    void shouldHandleNullAges() {
        SearchCount count = new SearchCount("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31), null, 0);

        assertAll(
                () -> assertNotNull(count.ages()),
                () -> assertTrue(count.ages().isEmpty())
        );
    }
}

