package com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest;

import com.emmanueljeanpierreauguste.riubackend.domain.model.HotelSearch;
import com.emmanueljeanpierreauguste.riubackend.domain.model.SearchCount;
import com.emmanueljeanpierreauguste.riubackend.domain.port.in.CreateSearchUseCase;
import com.emmanueljeanpierreauguste.riubackend.domain.port.in.GetSearchCountUseCase;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.mapper.SearchRestMapper;
import com.emmanueljeanpierreauguste.riubackend.infrastructure.adapter.in.rest.validator.SearchRequestValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SearchController.class)
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateSearchUseCase createSearchUseCase;

    @MockitoBean
    private GetSearchCountUseCase getSearchCountUseCase;

    @MockitoBean
    private SearchRestMapper searchRestMapper;

    @MockitoBean
    private SearchRequestValidator searchRequestValidator;

    @Test
    @DisplayName("POST /search should return searchId for valid request")
    void shouldReturnSearchIdForValidRequest() throws Exception {
        when(searchRequestValidator.validate(any())).thenReturn(List.of());
        when(searchRestMapper.toDomain(any())).thenReturn(
                new HotelSearch(null, "1234aBc",
                        LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                        List.of(30, 29, 1, 3)));
        when(createSearchUseCase.createSearch(any())).thenReturn("test-id-123");

        String requestBody = """
                {
                    "hotelId": "1234aBc",
                    "checkIn": "29/12/2023",
                    "checkOut": "31/12/2023",
                    "ages": [30, 29, 1, 3]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("test-id-123"));
    }

    @Test
    @DisplayName("POST /search should return 400 for missing hotelId")
    void shouldReturn400ForMissingHotelId() throws Exception {
        String requestBody = """
                {
                    "checkIn": "29/12/2023",
                    "checkOut": "31/12/2023",
                    "ages": [30, 29]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search should return 400 for empty ages")
    void shouldReturn400ForEmptyAges() throws Exception {
        String requestBody = """
                {
                    "hotelId": "1234aBc",
                    "checkIn": "29/12/2023",
                    "checkOut": "31/12/2023",
                    "ages": []
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search should return 400 for checkIn after checkOut")
    void shouldReturn400ForInvalidDates() throws Exception {
        when(searchRequestValidator.validate(any())).thenReturn(List.of("checkIn must be before checkOut"));

        String requestBody = """
                {
                    "hotelId": "1234aBc",
                    "checkIn": "31/12/2023",
                    "checkOut": "29/12/2023",
                    "ages": [30]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search should return 400 for negative ages")
    void shouldReturn400ForNegativeAges() throws Exception {
        when(searchRequestValidator.validate(any())).thenReturn(List.of("ages[0] must be >= 0"));

        String requestBody = """
                {
                    "hotelId": "1234aBc",
                    "checkIn": "29/12/2023",
                    "checkOut": "31/12/2023",
                    "ages": [-1, 30]
                }
                """;

        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /search should return 400 for malformed JSON")
    void shouldReturn400ForMalformedJson() throws Exception {
        mockMvc.perform(post("/search")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("not json"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /count should return count response")
    void shouldReturnCountResponse() throws Exception {
        SearchCount searchCount = new SearchCount("id1", "1234aBc",
                LocalDate.of(2023, 12, 29), LocalDate.of(2023, 12, 31),
                List.of(1, 3, 29, 30), 100);

        when(getSearchCountUseCase.getSearchCount("id1")).thenReturn(searchCount);
        when(searchRestMapper.toCountResponse(searchCount)).thenCallRealMethod();

        mockMvc.perform(get("/count").param("searchId", "id1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.searchId").value("id1"))
                .andExpect(jsonPath("$.count").value(100));
    }

    @Test
    @DisplayName("GET /count should return 400 when searchId is missing")
    void shouldReturn400WhenSearchIdMissing() throws Exception {
        mockMvc.perform(get("/count"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("GET /count should return 400 when searchId is blank")
    void shouldReturn400WhenSearchIdBlank() throws Exception {
        mockMvc.perform(get("/count").param("searchId", "   "))
                .andExpect(status().isBadRequest());
    }
}


