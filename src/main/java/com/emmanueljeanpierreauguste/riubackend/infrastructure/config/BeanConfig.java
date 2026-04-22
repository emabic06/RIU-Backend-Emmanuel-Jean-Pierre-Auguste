package com.emmanueljeanpierreauguste.riubackend.infrastructure.config;

import com.emmanueljeanpierreauguste.riubackend.application.port.in.CreateSearchUseCase;
import com.emmanueljeanpierreauguste.riubackend.application.port.in.GetSearchCountUseCase;
import com.emmanueljeanpierreauguste.riubackend.application.service.CreateSearchService;
import com.emmanueljeanpierreauguste.riubackend.application.service.GetSearchCountService;
import com.emmanueljeanpierreauguste.riubackend.application.service.PersistSearchService;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchEventPublisher;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring configuration that registers application-layer services as beans.
 * This keeps Spring annotations out of the application and domain layers,
 * respecting hexagonal architecture boundaries.
 */
@Configuration
public class BeanConfig {

    @Bean
    public CreateSearchUseCase createSearchUseCase(SearchEventPublisher searchEventPublisher) {
        return new CreateSearchService(searchEventPublisher);
    }

    @Bean
    public GetSearchCountUseCase getSearchCountUseCase(SearchRepository searchRepository) {
        return new GetSearchCountService(searchRepository);
    }

    @Bean
    public PersistSearchService persistSearchService(SearchRepository searchRepository) {
        return new PersistSearchService(searchRepository);
    }
}

