package com.emmanueljeanpierreauguste.riubackend.application.service;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
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
class PersistSearchServiceTest {

    @Mock
    private SearchRepository searchRepository;

    @InjectMocks
    private PersistSearchService persistSearchService;

    @Test
    @DisplayName("Should persist search using virtual thread")
    void shouldPersistSearch() throws InterruptedException {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        persistSearchService.persistSearch(search);

        Thread.sleep(500);

        verify(searchRepository, times(1)).save(search);
    }

    @Test
    @DisplayName("Should handle exception during persistence gracefully")
    void shouldHandleExceptionGracefully() throws InterruptedException {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30));

        doThrow(new RuntimeException("DB error")).when(searchRepository).save(search);

        persistSearchService.persistSearch(search);

        Thread.sleep(500);

        verify(searchRepository, times(1)).save(search);
    }
}

