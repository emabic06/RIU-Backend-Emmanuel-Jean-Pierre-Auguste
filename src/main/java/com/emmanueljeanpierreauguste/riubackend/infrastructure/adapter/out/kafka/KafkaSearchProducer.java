package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchEventPublisher;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper.SearchKafkaMapper;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka producer adapter implementing the SearchEventPublisher output port.
 * Publishes hotel search events to the hotel_availability_searches topic.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaSearchProducer implements SearchEventPublisher {

    private static final String TOPIC = "hotel_availability_searches";

    private final KafkaTemplate<String, SearchEventDto> kafkaTemplate;
    private final SearchKafkaMapper searchKafkaMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void publish(HotelSearch hotelSearch) {
        SearchEventDto dto = searchKafkaMapper.toDto(hotelSearch);
        kafkaTemplate.send(TOPIC, hotelSearch.searchId(), dto)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to publish search event to Kafka: {}", hotelSearch.searchId(), ex);
                        throw new RuntimeException("Failed to publish search event", ex);
                    }
                    log.info("Published search event to Kafka: {}", hotelSearch.searchId());
                });
    }
}

