package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.FlightResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightPlannerApplicationTests {
    @Autowired
    private MockMvc mvc;

    @Autowired
    FlightPlannerControllerAdmin flightPlannerControllerAdmin;
    @Autowired
    FlightPlannerService flightPlannerService;
    @Autowired
    FlightPlannerRepository flightPlannerRepository;

    static ObjectMapper jsonObjectMapper = new ObjectMapper();

    Airport from = new Airport("Latvia", "Riga", "RIX");
    Airport to = new Airport("Sweden", "Stockholm", "ARN");
    LocalDateTime departureTime = LocalDateTime.of(2019,1, 1, 0, 0);
    LocalDateTime arrivalTime = LocalDateTime.of(2019,1, 2, 0, 0);
    String carrier = "Ryanair";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Test
    void saveFlight() throws Exception {
        //Given
        FlightRequest request = new FlightRequest(from, to, carrier, formatter.format(departureTime), formatter.format(arrivalTime));
        //When
        MockHttpServletRequestBuilder requestBuilder = put("/admin-api/flights", request)
                .with(user("codelex-admin").password("Password123"))
                .content(jsonObjectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mvc.perform(requestBuilder)
                .andExpect(status().is(201))
                .andReturn();
        String responseContents = result.getResponse().getContentAsString();
        FlightResponse savedFlight = jsonObjectMapper.readValue(responseContents, FlightResponse.class);
        //Then
        assertNotNull(savedFlight.getId());
        assertEquals(savedFlight.getFrom(), from);
        assertEquals(savedFlight.getTo(), to);
        assertEquals(savedFlight.getCarrier(), carrier);
        assertEquals(savedFlight.getDepartureTime(), departureTime.format(formatter));
        assertEquals(savedFlight.getArrivalTime(), arrivalTime.format(formatter));
    }

    @Test
    void clearFlights() throws Exception {
        //When
        MockHttpServletRequestBuilder clearRequest = post("/testing-api/clear");

        MvcResult result = mvc.perform(clearRequest)
                .andExpect(status().isOk())
                .andReturn();
        // Then
        assertTrue(flightPlannerRepository.getFlights().isEmpty());
        assertTrue(flightPlannerRepository.getAllAirports().isEmpty());
    }
}
