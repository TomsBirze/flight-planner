package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;

import java.util.List;
import java.util.Optional;

public class FlightInDatabaseService implements FlightService {


    @Override
    public FlightResponse saveFlight(FlightRequest flightRequest) {
        return null;
    }

    @Override
    public void deleteFlight(Integer flightId) {

    }

    @Override
    public Optional<Flight> getFlightById(Integer id) {
        return Optional.empty();
    }

    @Override
    public void clearFlights() {

    }

    @Override
    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        return null;
    }

    @Override
    public Optional<FlightResponse> findFlightById(Integer flightId) {
        return Optional.empty();
    }

    @Override
    public List<Airport> searchAirports(String search) {
        return null;
    }
}
