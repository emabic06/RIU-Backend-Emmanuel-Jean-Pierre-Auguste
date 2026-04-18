package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

/**
 * Immutable request DTO for creating a hotel search.
 *
 * @param hotelId  the hotel identifier (must not be blank)
 * @param checkIn  the check-in date in dd/MM/yyyy format
 * @param checkOut the check-out date in dd/MM/yyyy format
 * @param ages     list of guest ages (must not be empty)
 */
@Schema(description = "Hotel availability search request")
public record SearchRequest(
        @NotBlank(message = "hotelId must not be blank")
        @Schema(description = "Hotel identifier", example = "1234aBc")
        String hotelId,

        @NotNull(message = "checkIn must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @Schema(description = "Check-in date (dd/MM/yyyy)", example = "29/12/2023")
        LocalDate checkIn,

        @NotNull(message = "checkOut must not be null")
        @JsonFormat(pattern = "dd/MM/yyyy")
        @Schema(description = "Check-out date (dd/MM/yyyy)", example = "31/12/2023")
        LocalDate checkOut,

        @NotEmpty(message = "ages must not be empty")
        @Schema(description = "List of guest ages", example = "[30, 29, 1, 3]")
        List<@NotNull(message = "age must not be null") Integer> ages
) {
    /**
     * Defensive copy of ages list.
     */
    public SearchRequest {
        ages = ages != null ? List.copyOf(ages) : List.of();
    }
}

