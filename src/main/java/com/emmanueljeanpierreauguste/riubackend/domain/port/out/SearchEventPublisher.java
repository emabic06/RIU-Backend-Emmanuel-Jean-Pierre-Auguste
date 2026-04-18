package com.emmanueljeanpierreauguste.riubackend.domain.port.out;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;

/**
 * Output port for publishing search events to a messaging system (Kafka).
 */
public interface SearchEventPublisher {

    /**
     * Publishes a hotel search event.
     *
     * @param hotelSearch the search to publish
     */
    void publish(HotelSearch hotelSearch);
}

