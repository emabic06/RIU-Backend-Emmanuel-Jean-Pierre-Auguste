package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Immutable error response DTO for API error handling.
 *
 * @param timestamp the time the error occurred
 * @param status    the HTTP status code
 * @param errors    list of error messages
 */
@Schema(description = "Error response")
public record ErrorResponse(
        @Schema(description = "Timestamp of the error")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code")
        int status,

        @Schema(description = "Error messages")
        List<String> errors
) {
    public ErrorResponse {
        errors = errors != null ? List.copyOf(errors) : List.of();
    }
}

