package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * JPA entity representing a persisted hotel search.
 * Uses parameterized queries via JPA to prevent SQL injection.
 */
@Entity
@Table(name = "hotel_searches")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SearchEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "search_id", nullable = false, unique = true)
    private String searchId;

    @Column(name = "hotel_id", nullable = false)
    private String hotelId;

    @Column(name = "check_in", nullable = false)
    private LocalDate checkIn;

    @Column(name = "check_out", nullable = false)
    private LocalDate checkOut;

    /**
     * Ages stored as a comma-separated string for simplicity.
     * Converted in the mapper, not in the getter (immutability principle).
     */
    @Column(name = "ages", nullable = false)
    private String ages;
}

