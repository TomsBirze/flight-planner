package io.codelex.flightplanner.flightPlannerCustomer;

import io.codelex.flightplanner.FlightPlannerRepository;
import io.codelex.flightplanner.configuration.CustomerRequestValidator;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import io.codelex.flightplanner.configuration.CustomFormater;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(FlightPlannerRepository.class.getName());
    private final FlightPlannerRepository flightPlannerRepository;
    private final CustomerRequestValidator customerRequestValidator;

    public CustomerService(FlightPlannerRepository flightPlannerRepository, CustomerRequestValidator customerRequestValidator) {
        this.flightPlannerRepository = flightPlannerRepository;
        this.customerRequestValidator = customerRequestValidator;
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        customerRequestValidator.validateNullFields(request);
        customerRequestValidator.validateSameAirports(request.getFrom(), request.getTo());
        return flightPlannerRepository.searchFlights(request);
    }

    public FlightResponse findFlightById(Integer flightId) {
        Optional<Flight> flightFromDatabase = flightPlannerRepository.findFlightById(flightId);
        logger.info("findFlightById() flightId: " + flightId + " found: " + flightFromDatabase);
        if (flightFromDatabase.isPresent()) {
            Flight flight = flightFromDatabase.get();

            String departureDateTime = CustomFormater.formatLocalDateTimeToString(flight.getDepartureTime());
            String arrivalDateTime = CustomFormater.formatLocalDateTimeToString(flight.getArrivalTime());

            return new FlightResponse(flight.getFrom(), flight.getTo(), flight.getCarrier(),
                    departureDateTime, arrivalDateTime, flight.getId());

        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Flight with ID " + flightId + " not found.");
        }
    }
    public List<Airport> searchAirports(String search) {
        return flightPlannerRepository.searchAirports(search);
    }

}
