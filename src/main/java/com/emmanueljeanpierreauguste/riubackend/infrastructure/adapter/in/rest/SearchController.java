package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest;

import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;
import com.emmanueljeanpierreauguste.riubackend.domain.port.in.CreateSearchUseCase;
import com.emmanueljeanpierreauguste.riubackend.domain.port.in.GetSearchCountUseCase;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.CountResponse;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.ErrorResponse;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.SearchRequest;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.SearchResponse;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.mapper.SearchRestMapper;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.validator.SearchRequestValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for hotel availability search operations.
 * Acts as an input adapter in the hexagonal architecture.
 */
@RestController
@RequiredArgsConstructor
@Tag(name = "Hotel Availability Search", description = "Endpoints for searching hotel availability and counting searches")
public class SearchController {

    private final CreateSearchUseCase createSearchUseCase;
    private final GetSearchCountUseCase getSearchCountUseCase;
    private final SearchRestMapper searchRestMapper;
    private final SearchRequestValidator searchRequestValidator;

    /**
     * Creates a new hotel availability search.
     * Validates the request, maps it to a domain model, and publishes it to Kafka.
     *
     * @param request the search request payload
     * @return the generated search ID
     */
    @PostMapping("/search")
    @Operation(
            summary = "Create a hotel availability search",
            description = "Validates the payload, generates a unique search ID, and publishes the search to Kafka",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search created successfully",
                            content = @Content(schema = @Schema(implementation = SearchResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid request payload",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> createSearch(@Valid @RequestBody SearchRequest request) {
        List<String> validationErrors = searchRequestValidator.validate(request);
        if (!validationErrors.isEmpty()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    validationErrors
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }

        String searchId = createSearchUseCase.createSearch(searchRestMapper.toDomain(request));
        return ResponseEntity.ok(new SearchResponse(searchId));
    }

    /**
     * Retrieves the count of identical searches for a given search ID.
     *
     * @param searchId the unique search identifier
     * @return the count response with search details
     */
    @GetMapping("/count")
    @Operation(
            summary = "Get count of identical searches",
            description = "Returns the number of searches with the same hotel, dates, and ages",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Count retrieved successfully",
                            content = @Content(schema = @Schema(implementation = CountResponse.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid search ID",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
                    @ApiResponse(responseCode = "404", description = "Search not found",
                            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
            }
    )
    public ResponseEntity<?> getCount(
            @Parameter(description = "The search ID returned by POST /search", required = true)
            @RequestParam String searchId) {
        if (searchId.isBlank()) {
            ErrorResponse errorResponse = new ErrorResponse(
                    LocalDateTime.now(),
                    HttpStatus.BAD_REQUEST.value(),
                    List.of("searchId must not be blank")
            );
            return ResponseEntity.badRequest().body(errorResponse);
        }

        SearchCount searchCount = getSearchCountUseCase.getSearchCount(searchId);
        return ResponseEntity.ok(searchRestMapper.toCountResponse(searchCount));
    }
}


