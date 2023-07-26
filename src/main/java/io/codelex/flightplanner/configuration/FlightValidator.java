package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.FlightInMemoryRepository;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class FlightValidator {
    private final FlightInMemoryRepository flightInMemoryRepository;

    public FlightValidator(FlightInMemoryRepository flightInMemoryRepository) {
        this.flightInMemoryRepository = flightInMemoryRepository;
    }
    private final DateTimeFormatter departureTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private final DateTimeFormatter arrivalTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public void validateFlight(FlightRequest flightRequest) {
        validateDepartureAirport(flightRequest);
        validateExistingFlight(flightRequest);
        validateArrivalTime(flightRequest);
    }

    private void validateExistingFlight(FlightRequest flightRequest) {
        List<Flight> flights = flightInMemoryRepository.getFlights();

        LocalDateTime requestDepartureTime = LocalDateTime.parse(flightRequest.getDepartureTime(), departureTimeFormatter);
        LocalDateTime requestArrivalTime = LocalDateTime.parse(flightRequest.getArrivalTime(), arrivalTimeFormatter);

        boolean flightAlreadyExists = flights.stream()
                .anyMatch(flight -> flight.getFrom().equals(flightRequest.getFrom()) &&
                        flight.getTo().equals(flightRequest.getTo()) &&
                        flight.getCarrier().equals(flightRequest.getCarrier()) &&
                        flight.getDepartureTime().equals(requestDepartureTime) &&
                        flight.getArrivalTime().equals(requestArrivalTime));

        if (flightAlreadyExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Invalid request.");
        }
    }
    private void validateDepartureAirport(FlightRequest flightRequest) {
        String departureAirport = flightRequest.getFrom().getAirport().trim();
        String arrivalAirport = flightRequest.getTo().getAirport().trim();

        if (departureAirport.equalsIgnoreCase(arrivalAirport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }
    private void validateArrivalTime(FlightRequest flightRequest) {
        LocalDateTime requestDepartureTime = LocalDateTime.parse(flightRequest.getDepartureTime(), departureTimeFormatter);
        LocalDateTime requestArrivalTime = LocalDateTime.parse(flightRequest.getArrivalTime(), arrivalTimeFormatter);

        if (requestArrivalTime.isBefore(requestDepartureTime) || requestArrivalTime.isEqual(requestDepartureTime)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }

    public void validateSameAirports(String fromAirport, String toAirport) {
        if (fromAirport.equalsIgnoreCase(toAirport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }

    public void validateNullFields(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request.");
        }
    }
}
