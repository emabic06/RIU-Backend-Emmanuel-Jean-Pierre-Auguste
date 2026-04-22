package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.port.out.SearchRepository;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.mapper.SearchPersistenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Persistence adapter implementing the SearchRepository output port.
 * Bridges the domain layer with the JPA infrastructure.
 */
@Component
@RequiredArgsConstructor
public class PostgresSearchRepository implements SearchRepository {

    private final JpaSearchRepository jpaSearchRepository;
    private final SearchPersistenceMapper searchPersistenceMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public void save(HotelSearch hotelSearch) {
        SearchEntity entity = searchPersistenceMapper.toEntity(hotelSearch);
        jpaSearchRepository.save(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<HotelSearch> findBySearchId(String searchId) {
        return jpaSearchRepository.findBySearchId(searchId)
                .map(searchPersistenceMapper::toDomain);
    }

    /**
     * {@inheritDoc}
     * Ages are stored as a comma-separated string preserving original order.
     */
    @Override
    public long countIdenticalSearches(HotelSearch hotelSearch) {
        String sortedAges = searchPersistenceMapper.agesToString(hotelSearch.ages());
        return jpaSearchRepository.countByHotelIdAndCheckInAndCheckOutAndAges(
                hotelSearch.hotelId(),
                hotelSearch.checkIn(),
                hotelSearch.checkOut(),
                sortedAges
        );
    }
}

