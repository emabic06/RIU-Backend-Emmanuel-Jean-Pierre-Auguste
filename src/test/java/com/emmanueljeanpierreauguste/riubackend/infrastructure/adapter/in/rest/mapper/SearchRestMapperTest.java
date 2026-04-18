package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.mapper;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.CountResponse;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.SearchRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchRestMapperTest {

    private final SearchRestMapper mapper = new SearchRestMapperImpl();

    @Test
    @DisplayName("Should convert SearchRequest to domain model")
    void shouldConvertToDomain() {
        SearchRequest request = new SearchRequest("hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 29, 1));

        HotelSearch search = mapper.toDomain(request);

        assertAll(
                () -> assertNull(search.searchId()),
                () -> assertEquals("hotel1", search.hotelId()),
                () -> assertEquals(LocalDate.of(2023, 12, 29), search.checkIn()),
                () -> assertEquals(LocalDate.of(2023, 12, 31), search.checkOut()),
                () -> assertEquals(List.of(30, 29, 1), search.ages())
        );
    }

    @Test
    @DisplayName("Should convert null SearchRequest to null")
    void shouldHandleNullRequest() {
        HotelSearch search = mapper.toDomain(null);
        assertNull(search);
    }

    @Test
    @DisplayName("Should convert SearchCount to CountResponse")
    void shouldConvertToCountResponse() {
        SearchCount searchCount = new SearchCount("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(1, 3, 29, 30), 50);

        CountResponse response = mapper.toCountResponse(searchCount);

        assertAll(
                () -> assertEquals("id1", response.searchId()),
                () -> assertEquals(50, response.count()),
                () -> assertEquals("hotel1", response.search().hotelId()),
                () -> assertEquals(LocalDate.of(2023, 12, 29), response.search().checkIn()),
                () -> assertEquals(LocalDate.of(2023, 12, 31), response.search().checkOut()),
                () -> assertEquals(List.of(1, 3, 29, 30), response.search().ages())
        );
    }
}
