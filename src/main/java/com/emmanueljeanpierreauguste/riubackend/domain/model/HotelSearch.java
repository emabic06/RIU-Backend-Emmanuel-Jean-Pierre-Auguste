package com.emmanueljeanpierreauguste.riubackend.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

/**
 * Immutable domain model representing a hotel availability search.
 * Uses a record to guarantee immutability.
 *
 * @param searchId unique identifier for this search
 * @param hotelId  hotel identifier
 * @param checkIn  check-in date
 * @param checkOut check-out date
 * @param ages     list of guest ages
 */
@Builder
public record HotelSearch(
        String searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages
) {
    /**
     * Compact constructor that enforces defensive copy of ages list
     * to guarantee immutability.
     */
    public HotelSearch {
        ages = ages != null ? List.copyOf(ages) : Collections.emptyList();
    }
}

