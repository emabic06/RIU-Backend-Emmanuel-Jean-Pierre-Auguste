package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto;

import java.util.List;

/**
 * Immutable DTO used for Kafka serialization/deserialization of search events.
 *
 * @param searchId unique search identifier
 * @param hotelId  hotel identifier
 * @param checkIn  check-in date as string (ISO format)
 * @param checkOut check-out date as string (ISO format)
 * @param ages     list of guest ages
 */
public record SearchEventDto(
        String searchId,
        String hotelId,
        String checkIn,
        String checkOut,
        List<Integer> ages
) {
    public SearchEventDto {
        ages = ages != null ? List.copyOf(ages) : List.of();
    }
}

