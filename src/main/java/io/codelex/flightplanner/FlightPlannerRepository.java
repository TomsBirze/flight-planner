package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.CustomFormater;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FlightPlannerRepository {
    private List<Flight> flightList = new ArrayList<>();
    private List<Airport> airportList = new ArrayList<>();

    public FlightPlannerRepository(List<Flight> flightList, List<Airport> airportList) {
        this.flightList = flightList;
        this.airportList = airportList;
    }

    //      Admin


    public synchronized FlightResponse saveFlight(Flight flight) {
        flightList.add(flight);

        String departureDateTime = CustomFormater.formatLocalDateTimeToString(flight.getDepartureTime());
        String arrivalDateTime = CustomFormater.formatLocalDateTimeToString(flight.getArrivalTime());

        return new FlightResponse(flight.getFrom(), flight.getTo(), flight.getCarrier(), departureDateTime, arrivalDateTime, flight.getId());
    }
    public synchronized List<Flight> getFlights() {
        return flightList;
    }

    public synchronized void deleteFlight(String flightId) {
        boolean removed = flightList.removeIf(fl -> flightId.equals(String.valueOf(fl.getId())));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with ID " + flightId + " not found.");
        }
    }

    public synchronized Optional<Flight> findFlightById(int id) {
        return flightList.stream()
                .filter(flight -> flight.getId() == id)
                .findFirst();
    }



//      Customer
    public synchronized List<Airport> searchAirports(String search) {
        return airportList.stream()
                .filter(airport -> airport.getAirport().contains(search) ||
                        airport.getCity().contains(search) ||
                        airport.getCountry().contains(search))
                .collect(Collectors.toList());
    }

    public synchronized PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        List<Flight> matchingFlights = flightList.stream()
                .filter(flight -> flight.getFrom().equals(request.getFrom()) &&
                        flight.getTo().equals(request.getTo()) &&
                        flight.getDepartureTime().equals(request.getDepartureDate()))
                .collect(Collectors.toList());

        return new PageResult<>(0, matchingFlights.size(), matchingFlights);
    }
    //      Testing
    public synchronized void clearFlights() {
        flightList.clear();
    }

}
