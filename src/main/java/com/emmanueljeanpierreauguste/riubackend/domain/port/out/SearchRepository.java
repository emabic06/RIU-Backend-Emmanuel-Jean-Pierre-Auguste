package com.emmanueljeanpierreauguste.riubackend.domain.port.out;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;

import java.util.Optional;

/**
 * Output port for persisting and retrieving hotel searches from the database.
 */
public interface SearchRepository {

    /**
     * Persists a hotel search.
     *
     * @param hotelSearch the search to save
     */
    void save(HotelSearch hotelSearch);

    /**
     * Finds a hotel search by its unique search ID.
     *
     * @param searchId the search identifier
     * @return the hotel search if found
     */
    Optional<HotelSearch> findBySearchId(String searchId);

    /**
     * Counts how many searches share the same hotelId, checkIn, checkOut and ages (sorted).
     *
     * @param hotelSearch the reference search
     * @return the count of identical searches
     */
    long countIdenticalSearches(HotelSearch hotelSearch);
}

