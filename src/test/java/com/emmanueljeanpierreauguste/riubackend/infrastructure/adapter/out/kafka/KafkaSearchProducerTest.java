package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper.SearchKafkaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaSearchProducerTest {

    @Mock
    private KafkaTemplate<String, SearchEventDto> kafkaTemplate;

    @Mock
    private SearchKafkaMapper searchKafkaMapper;

    @InjectMocks
    private KafkaSearchProducer kafkaSearchProducer;

    @Test
    @DisplayName("Should publish search event to Kafka topic")
    void shouldPublishEvent() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", List.of(30, 29));

        when(searchKafkaMapper.toDto(search)).thenReturn(dto);
        when(kafkaTemplate.send("hotel_availability_searches", "id1", dto))
                .thenReturn(CompletableFuture.completedFuture(null));

        kafkaSearchProducer.publish(search);

        verify(kafkaTemplate, times(1)).send("hotel_availability_searches", "id1", dto);
    }

    @Test
    @DisplayName("Should handle error when Kafka send fails")
    void shouldHandleKafkaSendError() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", List.of(30, 29));

        CompletableFuture<SendResult<String, SearchEventDto>> failedFuture =
                CompletableFuture.failedFuture(new RuntimeException("Kafka broker unavailable"));

        when(searchKafkaMapper.toDto(search)).thenReturn(dto);
        when(kafkaTemplate.send("hotel_availability_searches", "id1", dto))
                .thenReturn(failedFuture);

        kafkaSearchProducer.publish(search);

        verify(kafkaTemplate, times(1)).send("hotel_availability_searches", "id1", dto);
    }
}
