package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.exception.SearchNotFoundException;
import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.in.CreateSearchUseCase;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchEventPublisher;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Application service implementing the create search use case.
 * Generates a unique ID and publishes the search event to Kafka.
 */
@Service
@RequiredArgsConstructor
public class CreateSearchService implements CreateSearchUseCase {

    private final SearchEventPublisher searchEventPublisher;

    /**
     * {@inheritDoc}
     */
    @Override
    public String createSearch(HotelSearch hotelSearch) {
        String searchId = UUID.randomUUID().toString();

        HotelSearch searchWithId = HotelSearch.builder()
                .searchId(searchId)
                .hotelId(hotelSearch.hotelId())
                .checkIn(hotelSearch.checkIn())
                .checkOut(hotelSearch.checkOut())
                .ages(hotelSearch.ages())
                .build();

        searchEventPublisher.publish(searchWithId);
        return searchId;
    }
}

