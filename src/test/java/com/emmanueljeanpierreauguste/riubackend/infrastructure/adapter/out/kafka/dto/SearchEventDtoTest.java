package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchEventDtoTest {

    @Test
    @DisplayName("Should create immutable SearchEventDto with defensive copy")
    void shouldBeImmutable() {
        List<Integer> ages = new ArrayList<>(List.of(30, 29));
        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", ages);

        ages.add(99);

        assertAll(
                () -> assertEquals(2, dto.ages().size()),
                () -> assertThrows(UnsupportedOperationException.class, () -> dto.ages().add(5))
        );
    }

    @Test
    @DisplayName("Should handle null ages")
    void shouldHandleNullAges() {
        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", null);

        assertAll(
                () -> assertNotNull(dto.ages()),
                () -> assertTrue(dto.ages().isEmpty())
        );
    }
}

