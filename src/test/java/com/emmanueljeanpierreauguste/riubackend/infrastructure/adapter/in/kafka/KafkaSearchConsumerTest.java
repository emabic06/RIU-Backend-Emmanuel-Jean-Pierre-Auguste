package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.kafka;

import com.emmanueljeanpierreauguste.riubackend.application.service.PersistSearchService;
import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper.SearchKafkaMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaSearchConsumerTest {

    @Mock
    private PersistSearchService persistSearchService;

    @Mock
    private SearchKafkaMapper searchKafkaMapper;

    @InjectMocks
    private KafkaSearchConsumer kafkaSearchConsumer;

    @Test
    @DisplayName("Should consume event and delegate to persist service")
    void shouldConsumeAndPersist() {
        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", List.of(30, 29));
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        when(searchKafkaMapper.toDomain(dto)).thenReturn(search);

        kafkaSearchConsumer.consume(dto);

        verify(persistSearchService, times(1)).persistSearch(search);
    }
}

