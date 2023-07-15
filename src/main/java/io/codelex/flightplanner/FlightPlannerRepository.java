package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.FlightResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class FlightPlannerRepository {
    private final List<Flight> flightList;
    private final List<Airport> airportList;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public FlightPlannerRepository(List<Flight> flightList, List<Airport> airportList) {
        this.flightList = flightList;
        this.airportList = airportList;
    }

    public synchronized FlightResponse saveFlight(Flight flight) {
        flightList.add(flight);
        return new FlightResponse(flight.getFrom(),
                flight.getTo(),
                flight.getCarrier(),
                flight.getDepartureTime().format(formatter),
                flight.getArrivalTime().format(formatter),
                flight.getId());

       }

    public synchronized List<Flight> getFlights() {
        return flightList;
    }
    public synchronized List<Airport> getAirports() {
        return airportList;
    }

    public synchronized void deleteFlight(String flightId) {
        boolean removed = flightList.removeIf(fl -> flightId.equals(String.valueOf(fl.getId())));
        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public synchronized void clearFlights() {
        flightList.clear();
        airportList.clear();
    }

}
