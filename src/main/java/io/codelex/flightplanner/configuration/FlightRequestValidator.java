package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.FlightPlannerRepository;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FlightRequestValidator {
    private final FlightPlannerRepository flightPlannerRepository;

    public FlightRequestValidator(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }
    public void validateFlightRequest(FlightRequest flightRequest) {
        LocalDateTime departureDateTime = CustomFormater.formatStringToDateTime(flightRequest.getDepartureTime());
        LocalDateTime arrivalDateTime = CustomFormater.formatStringToDateTime(flightRequest.getArrivalTime());

        if (flightRequest.getFrom().getAirport().trim().equalsIgnoreCase(flightRequest.getTo().getAirport().trim())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Departure airport is the same as arrival.");
        }

        List<Flight> flights = flightPlannerRepository.getFlights();
        boolean flightAlreadyExists = flights.stream().anyMatch(fl ->
                fl.getFrom().equals(flightRequest.getFrom()) &&
                        fl.getTo().equals(flightRequest.getTo()) &&
                        fl.getCarrier().equals(flightRequest.getCarrier()) &&
                        fl.getDepartureTime().isEqual(departureDateTime) &&
                        fl.getArrivalTime().isEqual(arrivalDateTime)
        );

        if (flightAlreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This flight already exists in the database");
        }

        if (arrivalDateTime.isBefore(departureDateTime) || arrivalDateTime.isEqual(departureDateTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect arrival and departure dates.");
        }
    }
}
