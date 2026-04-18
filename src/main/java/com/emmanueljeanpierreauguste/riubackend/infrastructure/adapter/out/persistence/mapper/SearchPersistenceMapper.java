package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.mapper;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.out.persistence.SearchEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MapStruct mapper for converting between domain models and JPA entities.
 */
@Mapper(componentModel = "spring")
public interface SearchPersistenceMapper {

    /**
     * Converts a HotelSearch domain model to a SearchEntity.
     * Ages list is converted to a sorted comma-separated string in the constructor.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ages", expression = "java(agesToString(hotelSearch.ages()))")
    SearchEntity toEntity(HotelSearch hotelSearch);

    /**
     * Converts a SearchEntity to a HotelSearch domain model.
     */
    @Mapping(target = "ages", expression = "java(stringToAges(entity.getAges()))")
    HotelSearch toDomain(SearchEntity entity);

    /**
     * Converts a list of ages to a sorted comma-separated string.
     * Sorting ensures that count comparison works correctly.
     */
    default String agesToString(List<Integer> ages) {
        if (ages == null || ages.isEmpty()) {
            return "";
        }
        List<Integer> sorted = ages.stream().sorted().collect(Collectors.toList());
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < sorted.size(); i++) {
            if (i > 0) {
                sb.append(',');
            }
            sb.append(sorted.get(i));
        }
        return sb.toString();
    }

    /**
     * Converts a comma-separated string of ages to an unmodifiable list.
     */
    default List<Integer> stringToAges(String ages) {
        if (ages == null || ages.isBlank()) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(
                Arrays.stream(ages.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .collect(Collectors.toList())
        );
    }
}

