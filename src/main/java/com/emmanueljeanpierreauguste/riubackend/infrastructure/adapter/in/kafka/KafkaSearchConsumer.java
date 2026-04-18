package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.kafka;

import com.emmanueljeanpierreauguste.riubackend.application.service.PersistSearchService;
import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper.SearchKafkaMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

/**
 * Kafka consumer adapter that listens to the hotel_availability_searches topic.
 * Receives search events and delegates persistence to the application service.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSearchConsumer {

    private final PersistSearchService persistSearchService;
    private final SearchKafkaMapper searchKafkaMapper;

    /**
     * Consumes search events from the Kafka topic and persists them.
     *
     * @param dto the search event received from Kafka
     */
    @KafkaListener(topics = "hotel_availability_searches", groupId = "riubackend-group")
    public void consume(SearchEventDto dto) {
        log.info("Received search event from Kafka: {}", dto.searchId());
        HotelSearch hotelSearch = searchKafkaMapper.toDomain(dto);
        persistSearchService.persistSearch(hotelSearch);
    }
}

