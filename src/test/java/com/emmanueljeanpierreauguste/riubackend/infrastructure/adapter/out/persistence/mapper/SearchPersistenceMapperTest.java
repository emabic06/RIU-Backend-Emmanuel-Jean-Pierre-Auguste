package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.mapper;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.SearchEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SearchPersistenceMapperTest {

    private final SearchPersistenceMapper mapper = new SearchPersistenceMapperImpl();

    @Test
    @DisplayName("Should convert domain to entity with sorted ages string")
    void shouldConvertToEntity() {
        HotelSearch search = new HotelSearch("id1", "hotel1",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(30, 1, 29, 3));

        SearchEntity entity = mapper.toEntity(search);

        assertAll(
                () -> assertEquals("id1", entity.getSearchId()),
                () -> assertEquals("hotel1", entity.getHotelId()),
                () -> assertEquals("1,3,29,30", entity.getAges()),
                () -> assertNull(entity.getId())
        );
    }

    @Test
    @DisplayName("Should convert entity to domain")
    void shouldConvertToDomain() {
        SearchEntity entity = SearchEntity.builder()
                .id(1L).searchId("id1").hotelId("hotel1")
                .checkIn(LocalDate.of(2023, 12, 29))
                .checkOut(LocalDate.of(2023, 12, 31))
                .ages("1,3,29,30").build();

        HotelSearch search = mapper.toDomain(entity);

        assertAll(
                () -> assertEquals("id1", search.searchId()),
                () -> assertEquals(List.of(1, 3, 29, 30), search.ages())
        );
    }

    @Test
    @DisplayName("Should handle empty ages")
    void shouldHandleEmptyAges() {
        assertAll(
                () -> assertEquals("", mapper.agesToString(List.of())),
                () -> assertEquals("", mapper.agesToString(null)),
                () -> assertTrue(mapper.stringToAges(null).isEmpty()),
                () -> assertTrue(mapper.stringToAges("").isEmpty()),
                () -> assertTrue(mapper.stringToAges("  ").isEmpty())
        );
    }

    @Test
    @DisplayName("Should return null for null HotelSearch input")
    void shouldReturnNullForNullDomain() {
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should return null for null SearchEntity input")
    void shouldReturnNullForNullEntity() {
        assertNull(mapper.toDomain(null));
    }
}
