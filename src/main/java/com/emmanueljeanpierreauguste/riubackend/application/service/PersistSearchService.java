package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service responsible for persisting hotel searches
 * received from the Kafka consumer.
 * Uses virtual threads for database persistence.
 */
@RequiredArgsConstructor
@Slf4j
public class PersistSearchService {

    private final SearchRepository searchRepository;

    /**
     * Persists a hotel search using a virtual thread.
     *
     * @param hotelSearch the search to persist
     */
    public void persistSearch(HotelSearch hotelSearch) {
        Thread.startVirtualThread(() -> {
            try {
                searchRepository.save(hotelSearch);
                log.info("Search persisted successfully: {}", hotelSearch.searchId());
            } catch (Exception e) {
                log.error("Error persisting search: {}", hotelSearch.searchId(), e);
            }
        });
    }
}

