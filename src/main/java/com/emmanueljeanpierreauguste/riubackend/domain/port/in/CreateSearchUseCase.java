package com.emmanueljeanpierreauguste.riubackend.domain.port.in;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;

/**
 * Input port for creating a hotel availability search.
 * Application layer implements this use case.
 */
public interface CreateSearchUseCase {

    /**
     * Creates a new search, publishes it to the messaging system,
     * and returns the assigned search ID.
     *
     * @param hotelSearch the search to create (without searchId)
     * @return the generated unique search ID
     */
    String createSearch(HotelSearch hotelSearch);
}

