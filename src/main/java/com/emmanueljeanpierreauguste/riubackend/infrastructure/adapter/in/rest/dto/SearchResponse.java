package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Immutable response DTO containing the generated search ID.
 *
 * @param searchId the unique search identifier
 */
@Schema(description = "Search creation response")
public record SearchResponse(
        @Schema(description = "Unique search identifier", example = "550e8400-e29b-41d4-a716-446655440000")
        String searchId
) {
}

