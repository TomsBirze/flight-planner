package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.domain.SearchFlightsRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class FlightPlannerRepository {
    private final List<Flight> flightList;
    private final List<Airport> airportList;

    public FlightPlannerRepository(List<Flight> flightList, List<Airport> airportList) {
        this.flightList = flightList;
        this.airportList = airportList;
    }
    public void clearFlights() {
        flightList.clear();
    }
    public void addFlight(Flight flight) {
        flightList.add(flight);
    }
    public List<Flight> getFlightList() {
        return flightList;
    }
    public Optional<Flight> findFlightById(int id) {
        return flightList.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst();
    }
    public void removeFlight(Flight flight) {
        flightList.remove(flight);
    }

    public List<Airport> searchAirports(String search) {
        return airportList.stream()
                .filter(airport -> airport.getAirport().contains(search) ||
                        airport.getCity().contains(search) ||
                        airport.getCountry().contains(search))
                .collect(Collectors.toList());
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        List<Flight> matchingFlights = flightList.stream()
                .filter(flight -> flight.getFrom().equals(request.getFrom()) &&
                        flight.getTo().equals(request.getTo()) &&
                        flight.getDepartureTime().equals(request.getDepartureDate()))
                .collect(Collectors.toList());

        return new PageResult<>(0, matchingFlights.size(), matchingFlights);
    }
}
