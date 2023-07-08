package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.CustomFormater;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class FlightPlannerRepository {
    private static final Logger logger = LoggerFactory.getLogger(FlightPlannerRepository.class.getName());
    private final List<Flight> flightList;
    private final Map<String, Airport> allAirports;

    public FlightPlannerRepository(List<Flight> flightList, Map<String, Airport> allAirports) {
        this.flightList = flightList;
        this.allAirports = allAirports;
    }

    //      Admin

    public synchronized FlightResponse saveFlight(Flight flight) {
        flightList.add(flight);

        addAirports(flight);

        String departureDateTime = CustomFormater.formatLocalDateTimeToString(flight.getDepartureTime());
        String arrivalDateTime = CustomFormater.formatLocalDateTimeToString(flight.getArrivalTime());

        return new FlightResponse(flight.getFrom(), flight.getTo(), flight.getCarrier(), departureDateTime, arrivalDateTime, flight.getId());
    }
    public Optional<Flight> findFlightById(Integer id) {
        return flightList.stream()
                .filter(flight -> Objects.equals(flight.getId(), id))
                .findFirst();
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
    private void addAirports(Flight flight) {
        String airportFrom = flight.getFrom().getCountry() + " " +
                flight.getFrom().getCity() + " " +
                flight.getFrom().getAirport();

        String airportTo = flight.getTo().getCountry() + " " +
                flight.getTo().getCity() + " " +
                flight.getTo().getAirport();

        allAirports.put(airportFrom, flight.getFrom());
        allAirports.put(airportTo, flight.getTo());
    }

//      Customer

    public List<Airport> searchAirports(String search) {
        List<Airport> foundAirports = new ArrayList<>();
        for (Map.Entry<String, Airport> entry : allAirports.entrySet()) {
            if (entry.getKey().toLowerCase().trim().contains(search.toLowerCase().trim())) {
                foundAirports.add(entry.getValue());
            }
        }
        logger.info("search Airports: " + search + " found: " + foundAirports);
        return foundAirports;
}


    public synchronized PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        logger.info("Searching flights with request: " + request.toString());
        PageResult<Flight> result = new PageResult<>(0, 0, new ArrayList<>());

        if (request.getFrom().equals(request.getTo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid from and to are equal.");
        }

        DateTimeFormatter flightDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Flight> foundFlights = flightList.stream()
                .filter(fl -> request.getFrom().equals(fl.getFrom().getAirport()) &&
                        request.getTo().equals(fl.getTo().getAirport()) &&
                        fl.getDepartureTime().format(flightDateFormatter).equals(request.getDepartureDate()))
                .collect(Collectors.toList());

        logger.info("Found flights: " + foundFlights.toString());

        result.setTotalItems(foundFlights.size());
        result.setItems(foundFlights);
        result.setPage(foundFlights.size() / 10);
        return result;
    }
    //      Testing
    public synchronized void clearFlights() {
        flightList.clear();
    }

}
