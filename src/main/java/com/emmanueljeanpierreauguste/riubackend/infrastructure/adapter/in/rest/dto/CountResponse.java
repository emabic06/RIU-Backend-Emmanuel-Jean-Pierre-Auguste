package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;

/**
 * Immutable response DTO containing the search count result.
 *
 * @param searchId the unique search identifier
 * @param search   the search details
 * @param count    the number of identical searches
 */
@Schema(description = "Search count response")
public record CountResponse(
        @Schema(description = "Unique search identifier")
        String searchId,

        @Schema(description = "Search details")
        SearchDetail search,

        @Schema(description = "Number of identical searches", example = "100")
        long count
) {
    /**
     * Immutable nested record with the search details.
     *
     * @param hotelId  hotel identifier
     * @param checkIn  check-in date
     * @param checkOut check-out date
     * @param ages     sorted list of guest ages
     */
    @Schema(description = "Search detail information")
    public record SearchDetail(
            @Schema(description = "Hotel identifier", example = "1234aBc")
            String hotelId,

            @JsonFormat(pattern = "dd/MM/yyyy")
            @Schema(description = "Check-in date", example = "29/12/2023")
            LocalDate checkIn,

            @JsonFormat(pattern = "dd/MM/yyyy")
            @Schema(description = "Check-out date", example = "31/12/2023")
            LocalDate checkOut,

            @Schema(description = "Sorted list of guest ages", example = "[1, 3, 29, 30]")
            List<Integer> ages
    ) {
        public SearchDetail {
            ages = ages != null ? List.copyOf(ages) : List.of();
        }
    }
}

