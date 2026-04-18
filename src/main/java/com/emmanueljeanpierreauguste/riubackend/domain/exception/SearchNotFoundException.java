package com.emmanueljeanpierreauguste.riubackend.domain.exception;

/**
 * Exception thrown when a search is not found by its ID.
 */
public class SearchNotFoundException extends RuntimeException {

    public SearchNotFoundException(String searchId) {
        super("Search not found with id: " + searchId);
    }
}

