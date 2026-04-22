package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.exception.SearchNotFoundException;
import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;
import com.emmanueljeanpierreauguste.riubackend.application.port.in.GetSearchCountUseCase;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import lombok.RequiredArgsConstructor;

/**
 * Application service implementing the get search count use case.
 * Retrieves the search and counts how many identical searches exist.
 */
@RequiredArgsConstructor
public class GetSearchCountService implements GetSearchCountUseCase {

    private final SearchRepository searchRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public SearchCount getSearchCount(String searchId) {
        HotelSearch hotelSearch = searchRepository.findBySearchId(searchId)
                .orElseThrow(() -> new SearchNotFoundException(searchId));

        long count = searchRepository.countIdenticalSearches(hotelSearch);

        return SearchCount.builder()
                .searchId(searchId)
                .hotelId(hotelSearch.hotelId())
                .checkIn(hotelSearch.checkIn())
                .checkOut(hotelSearch.checkOut())
                .ages(hotelSearch.ages())
                .count(count)
                .build();
    }
}
