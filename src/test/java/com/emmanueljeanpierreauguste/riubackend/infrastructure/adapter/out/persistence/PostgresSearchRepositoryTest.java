package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.mapper.SearchPersistenceMapper;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostgresSearchRepositoryTest {

    @Mock
    private JpaSearchRepository jpaSearchRepository;

    @Mock
    private SearchPersistenceMapper searchPersistenceMapper;

    @InjectMocks
    private PostgresSearchRepository postgresSearchRepository;

    private final LocalDate checkIn = LocalDate.of(2023, 12, 29);
    private final LocalDate checkOut = LocalDate.of(2023, 12, 31);

    @Test
    @DisplayName("Should save search entity")
    void shouldSave() {
        HotelSearch search = new HotelSearch("id1", "hotel1", checkIn, checkOut, List.of(30, 29));
        SearchEntity entity = SearchEntity.builder()
                .searchId("id1").hotelId("hotel1").checkIn(checkIn).checkOut(checkOut).ages("29,30").build();

        when(searchPersistenceMapper.toEntity(search)).thenReturn(entity);

        postgresSearchRepository.save(search);

        verify(jpaSearchRepository, times(1)).save(entity);
    }

    @Test
    @DisplayName("Should find by searchId")
    void shouldFindBySearchId() {
        SearchEntity entity = SearchEntity.builder()
                .searchId("id1").hotelId("hotel1").checkIn(checkIn).checkOut(checkOut).ages("29,30").build();
        HotelSearch search = new HotelSearch("id1", "hotel1", checkIn, checkOut, List.of(29, 30));

        when(jpaSearchRepository.findBySearchId("id1")).thenReturn(Optional.of(entity));
        when(searchPersistenceMapper.toDomain(entity)).thenReturn(search);

        Optional<HotelSearch> result = postgresSearchRepository.findBySearchId("id1");

        assertAll(
                () -> assertTrue(result.isPresent()),
                () -> assertEquals("id1", result.get().searchId())
        );
    }

    @Test
    @DisplayName("Should return empty when not found")
    void shouldReturnEmptyWhenNotFound() {
        when(jpaSearchRepository.findBySearchId("unknown")).thenReturn(Optional.empty());

        Optional<HotelSearch> result = postgresSearchRepository.findBySearchId("unknown");
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should count identical searches")
    void shouldCountIdentical() {
        HotelSearch search = new HotelSearch("id1", "hotel1", checkIn, checkOut, List.of(30, 29));

        when(searchPersistenceMapper.agesToString(search.ages())).thenReturn("29,30");
        when(jpaSearchRepository.countByHotelIdAndCheckInAndCheckOutAndAges(
                "hotel1", checkIn, checkOut, "29,30")).thenReturn(5L);

        long count = postgresSearchRepository.countIdenticalSearches(search);
        assertEquals(5, count);
    }
}

