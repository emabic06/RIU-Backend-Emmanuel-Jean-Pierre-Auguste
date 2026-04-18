package com.emmanueljeanpierreauguste.riubackend.domain.model;

import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * Immutable domain model representing the count result for a search.
 *
 * @param searchId unique search identifier
 * @param hotelId  hotel identifier
 * @param checkIn  check-in date
 * @param checkOut check-out date
 * @param ages     sorted list of guest ages
 * @param count    number of identical searches
 */
@Builder
public record SearchCount(
        String searchId,
        String hotelId,
        LocalDate checkIn,
        LocalDate checkOut,
        List<Integer> ages,
        long count
) {
    public SearchCount {
        ages = ages != null ? List.copyOf(ages) : List.of();
    }
}

