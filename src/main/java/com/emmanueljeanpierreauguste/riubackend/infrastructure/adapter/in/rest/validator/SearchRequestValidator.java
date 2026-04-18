package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.validator;

import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.dto.SearchRequest;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Custom validator for SearchRequest business rules
 * that go beyond standard Bean Validation annotations.
 */
@Component
public class SearchRequestValidator {

    /**
     * Validates business rules for the search request.
     *
     * @param request the search request to validate
     * @return an unmodifiable list of validation error messages (empty if valid)
     */
    public List<String> validate(SearchRequest request) {
        List<String> errors = new ArrayList<>();

        if (request.checkIn() != null && request.checkOut() != null
                && !request.checkIn().isBefore(request.checkOut())) {
            errors.add("checkIn must be before checkOut");
        }

        if (request.ages() != null) {
            for (int i = 0; i < request.ages().size(); i++) {
                if (request.ages().get(i) < 0) {
                    errors.add("ages[%d] must be >= 0".formatted(i));
                }
            }
        }

        return Collections.unmodifiableList(errors);
    }
}


