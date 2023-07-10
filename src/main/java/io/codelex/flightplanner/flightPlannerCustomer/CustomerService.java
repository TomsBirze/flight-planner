package io.codelex.flightplanner.flightPlannerCustomer;

import io.codelex.flightplanner.FlightPlannerRepository;
import io.codelex.flightplanner.configuration.SearchFlightsValidator;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import io.codelex.flightplanner.configuration.CustomFormatter;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(FlightPlannerRepository.class.getName());
    private final FlightPlannerRepository flightPlannerRepository;
    private final SearchFlightsValidator searchFlightsValidator;

    public CustomerService(FlightPlannerRepository flightPlannerRepository, SearchFlightsValidator searchFlightsValidator) {
        this.flightPlannerRepository = flightPlannerRepository;
        this.searchFlightsValidator = searchFlightsValidator;
    }
    public synchronized PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        logger.info("Searching flights with request: " + request.toString());
        PageResult<Flight> result = new PageResult<>(0, 0, new ArrayList<>());
        searchFlightsValidator.validateNullFields(request);
        searchFlightsValidator.validateSameAirports(request.getFrom(), request.getTo());

        DateTimeFormatter flightDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        List<Flight> foundFlights = flightPlannerRepository.getFlights()
                .stream()
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

    public synchronized Optional<FlightResponse> findFlightById(Integer flightId) {
        Optional<Flight> flightFromDatabase = Optional.ofNullable(flightPlannerRepository
                .getFlights()
                .stream()
                .filter(flight -> Objects.equals(flight.getId(), flightId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        logger.info("findFlightById() flightId: " + flightId + " found: " + flightFromDatabase);

            Flight flight = flightFromDatabase.get();

            String departureDateTime = CustomFormatter.formatLocalDateTimeToString(flight.getDepartureTime());
            String arrivalDateTime = CustomFormatter.formatLocalDateTimeToString(flight.getArrivalTime());

            return Optional.of(new FlightResponse(
                    flight.getFrom(),
                    flight.getTo(),
                    flight.getCarrier(),
                    departureDateTime,
                    arrivalDateTime,
                    flight.getId()));
    }
    public synchronized List<Airport> searchAirports(String search) {
        List<Airport> foundAirports = flightPlannerRepository.getAllAirports()
                .values()
                .stream()
                .filter(airport ->
                        airport.getAirport().toLowerCase().contains(search)
                                || airport.getCity().toLowerCase().contains(search)
                                || airport.getCountry().toLowerCase().contains(search)
                )
                .map(airport -> new Airport(airport.getCountry(), airport.getCity(), airport.getAirport()))
                .collect(Collectors.toList());

        logger.info("Search Airports: " + search + ", Found Airports: " + foundAirports);
        return foundAirports;
    }

}
