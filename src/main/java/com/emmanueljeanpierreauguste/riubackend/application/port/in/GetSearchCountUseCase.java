package com.emmanueljeanpierreauguste.riubackend.application.port.in;

import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;

/**
 * Input port for retrieving the count of identical searches.
 */
public interface GetSearchCountUseCase {

    /**
     * Returns the count of searches matching the given searchId.
     *
     * @param searchId the unique search identifier
     * @return the search count result
     */
    SearchCount getSearchCount(String searchId);
}

