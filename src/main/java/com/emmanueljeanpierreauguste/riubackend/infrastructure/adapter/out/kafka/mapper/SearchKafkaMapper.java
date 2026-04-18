package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.mapper;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.kafka.dto.SearchEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * MapStruct mapper for converting between domain models and Kafka DTOs.
 */
@Mapper(componentModel = "spring")
public interface SearchKafkaMapper {

    /**
     * Converts a HotelSearch domain model to a Kafka event DTO.
     */
    @Mapping(target = "checkIn", expression = "java(dateToString(hotelSearch.checkIn()))")
    @Mapping(target = "checkOut", expression = "java(dateToString(hotelSearch.checkOut()))")
    SearchEventDto toDto(HotelSearch hotelSearch);

    /**
     * Converts a Kafka event DTO to a HotelSearch domain model.
     */
    @Mapping(target = "checkIn", expression = "java(stringToDate(dto.checkIn()))")
    @Mapping(target = "checkOut", expression = "java(stringToDate(dto.checkOut()))")
    HotelSearch toDomain(SearchEventDto dto);

    /**
     * Converts a LocalDate to an ISO date string.
     */
    default String dateToString(LocalDate date) {
        return date != null ? date.format(DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }

    /**
     * Converts an ISO date string to a LocalDate.
     */
    default LocalDate stringToDate(String date) {
        return date != null ? LocalDate.parse(date, DateTimeFormatter.ISO_LOCAL_DATE) : null;
    }
}

