package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchEventPublisher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateSearchServiceTest {

    @Mock
    private SearchEventPublisher searchEventPublisher;

    @InjectMocks
    private CreateSearchService createSearchService;

    @Test
    @DisplayName("Should generate unique search ID and publish event")
    void shouldCreateSearchAndPublish() {
        HotelSearch input = new HotelSearch(null, "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3));

        String searchId = createSearchService.createSearch(input);

        ArgumentCaptor<HotelSearch> captor = ArgumentCaptor.forClass(HotelSearch.class);
        verify(searchEventPublisher, times(1)).publish(captor.capture());

        HotelSearch published = captor.getValue();
        assertAll(
                () -> assertNotNull(searchId),
                () -> assertFalse(searchId.isBlank()),
                () -> assertEquals(searchId, published.searchId()),
                () -> assertEquals("hotel1", published.hotelId()),
                () -> assertEquals(List.of(30, 29, 1, 3), published.ages())
        );
    }

    @Test
    @DisplayName("Should generate different IDs for identical searches")
    void shouldGenerateUniqueIds() {
        HotelSearch input = new HotelSearch(null, "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        String id1 = createSearchService.createSearch(input);
        String id2 = createSearchService.createSearch(input);

        assertNotEquals(id1, id2);
    }
}

