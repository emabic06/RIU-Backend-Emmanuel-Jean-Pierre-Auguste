package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchKafkaMapperTest {

    private final SearchKafkaMapper mapper = new SearchKafkaMapperImpl();

    @Test
    @DisplayName("Should convert domain to DTO")
    void shouldConvertToDto() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29));

        SearchEventDto dto = mapper.toDto(search);

        assertAll(
                () -> assertEquals("id1", dto.searchId()),
                () -> assertEquals("hotel1", dto.hotelId()),
                () -> assertEquals("2023-12-29", dto.checkIn()),
                () -> assertEquals("2023-12-31", dto.checkOut()),
                () -> assertEquals(List.of(30, 29), dto.ages())
        );
    }

    @Test
    @DisplayName("Should convert DTO to domain")
    void shouldConvertToDomain() {
        SearchEventDto dto = new SearchEventDto("id1", "hotel1", "2023-12-29", "2023-12-31", List.of(30, 29));

        HotelSearch search = mapper.toDomain(dto);

        assertAll(
                () -> assertEquals("id1", search.searchId()),
                () -> assertEquals(LocalDate.of(2023, 12, 29), search.checkIn()),
                () -> assertEquals(LocalDate.of(2023, 12, 31), search.checkOut()),
                () -> assertEquals(List.of(30, 29), search.ages())
        );
    }

    @Test
    @DisplayName("Should handle null dates")
    void shouldHandleNullDates() {
        assertAll(
                () -> assertNull(mapper.dateToString(null)),
                () -> assertNull(mapper.stringToDate(null))
        );
    }

    @Test
    @DisplayName("Should return null for null HotelSearch input")
    void shouldReturnNullForNullHotelSearch() {
        assertNull(mapper.toDto(null));
    }

    @Test
    @DisplayName("Should return null for null SearchEventDto input")
    void shouldReturnNullForNullDto() {
        assertNull(mapper.toDomain(null));
    }
}
