package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.CustomFormatter;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.FlightResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Repository
public class FlightPlannerRepository {
    private final List<Flight> flightList;
    private final Map<String, Airport> allAirports;

    public FlightPlannerRepository(List<Flight> flightList, Map<String, Airport> allAirports) {
        this.flightList = flightList;
        this.allAirports = allAirports;
    }

    public synchronized FlightResponse saveFlight(Flight flight) {
        flightList.add(flight);

        addAirports(flight);

        String departureDateTime = CustomFormatter.formatLocalDateTimeToString(flight.getDepartureTime());
        String arrivalDateTime = CustomFormatter.formatLocalDateTimeToString(flight.getArrivalTime());

        return new FlightResponse(flight.getFrom(), flight.getTo(), flight.getCarrier(), departureDateTime, arrivalDateTime, flight.getId());
    }

    public synchronized List<Flight> getFlights() {
        return flightList;
    }
    public synchronized Map<String, Airport> getAllAirports() {
        return allAirports;
    }

    public synchronized void deleteFlight(String flightId) {
        boolean removed = flightList.removeIf(fl -> flightId.equals(String.valueOf(fl.getId())));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with ID " + flightId + " not found.");
        }
    }
    private synchronized void addAirports(Flight flight) {
        String airportFrom = flight.getFrom().getCountry() + " " +
                flight.getFrom().getCity() + " " +
                flight.getFrom().getAirport();

        String airportTo = flight.getTo().getCountry() + " " +
                flight.getTo().getCity() + " " +
                flight.getTo().getAirport();

        allAirports.put(airportFrom, flight.getFrom());
        allAirports.put(airportTo, flight.getTo());
    }

    public synchronized void clearFlights() {
        flightList.clear();
        allAirports.clear();
    }

}
