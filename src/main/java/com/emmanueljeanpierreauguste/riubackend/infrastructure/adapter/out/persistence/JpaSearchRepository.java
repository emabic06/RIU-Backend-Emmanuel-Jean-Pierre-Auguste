package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Spring Data JPA repository for SearchEntity.
 * Uses parameterized queries to prevent SQL injection.
 */
@Repository
public interface JpaSearchRepository extends JpaRepository<SearchEntity, Long> {

    /**
     * Finds a search entity by its unique search ID.
     */
    Optional<SearchEntity> findBySearchId(String searchId);

    /**
     * Counts identical searches based on hotelId, checkIn, checkOut and ages string.
     * Ages are stored as a sorted comma-separated string so order matters for counting.
     */
    @Query("SELECT COUNT(s) FROM SearchEntity s WHERE s.hotelId = :hotelId " +
            "AND s.checkIn = :checkIn AND s.checkOut = :checkOut AND s.ages = :ages")
    long countByHotelIdAndCheckInAndCheckOutAndAges(
            @Param("hotelId") String hotelId,
            @Param("checkIn") LocalDate checkIn,
            @Param("checkOut") LocalDate checkOut,
            @Param("ages") String ages);
}

