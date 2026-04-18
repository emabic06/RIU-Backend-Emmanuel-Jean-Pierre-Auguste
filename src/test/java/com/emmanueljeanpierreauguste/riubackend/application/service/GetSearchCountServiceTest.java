package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.exception.SearchNotFoundException;
import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetSearchCountServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private GetSearchCountService getSearchCountService;

    @Test
    @DisplayName("Should return count with sorted ages")
    void shouldReturnCountWithSortedAges() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1, 3));

        when(searchRepository.findBySearchId("id1")).thenReturn(Optional.of(search));
        when(searchRepository.countIdenticalSearches(search)).thenReturn(5L);

        SearchCount result = getSearchCountService.getSearchCount("id1");

        assertAll(
                () -> assertEquals("id1", result.searchId()),
                () -> assertEquals("hotel1", result.hotelId()),
                () -> assertEquals(5, result.count()),
                () -> assertEquals(List.of(1, 3, 29, 30), result.ages())
        );
    }

    @Test
    @DisplayName("Should throw SearchNotFoundException when search not found")
    void shouldThrowWhenNotFound() {
        when(searchRepository.findBySearchId("unknown")).thenReturn(Optional.empty());

        assertThrows(SearchNotFoundException.class,
                () -> getSearchCountService.getSearchCount("unknown"));
    }
}

