package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface FlightService {

    FlightResponse saveFlight(FlightRequest flightRequest);
    void deleteFlight(Integer flightId);
    Optional<Flight> getFlightById(Integer id);
    void clearFlights();
    PageResult<Flight> searchFlights(SearchFlightsRequest request);
    Optional<FlightResponse> findFlightById(Integer flightId);
    List<Airport> searchAirports(String search);
}
