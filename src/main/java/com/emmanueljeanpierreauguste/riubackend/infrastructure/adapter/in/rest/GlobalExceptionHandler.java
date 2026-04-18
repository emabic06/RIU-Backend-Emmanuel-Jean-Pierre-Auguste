package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest;

import com.emmanueljeanpierreauguste.riubackend.domain.exception.SearchNotFoundException;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Global exception handler for REST API errors.
 * Converts exceptions to standardized ErrorResponse DTOs.
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Handles Bean Validation errors from @Valid annotations.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .toList();
        return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(), errors)
        );
    }

    /**
     * Handles malformed JSON or unparseable request bodies.
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleMessageNotReadable(HttpMessageNotReadableException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        List.of("Malformed request body: " + ex.getMostSpecificCause().getMessage()))
        );
    }

    /**
     * Handles missing required request parameters.
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
        return ResponseEntity.badRequest().body(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                        List.of(ex.getMessage()))
        );
    }

    /**
     * Handles search not found exceptions.
     */
    @ExceptionHandler(SearchNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSearchNotFound(SearchNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
                        List.of(ex.getMessage()))
        );
    }

    /**
     * Handles all unexpected exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        List.of("Internal server error"))
        );
    }
}

