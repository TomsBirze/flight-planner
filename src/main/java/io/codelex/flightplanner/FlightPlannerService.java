package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.CustomFormater;
import io.codelex.flightplanner.configuration.FlightRequestValidator;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.FlightResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class FlightPlannerService {
    private final FlightPlannerRepository flightPlannerRepository;
    private final FlightRequestValidator flightRequestValidator;

    public FlightPlannerService(FlightPlannerRepository flightPlannerRepository, FlightRequestValidator flightRequestValidator) {
        this.flightPlannerRepository = flightPlannerRepository;
        this.flightRequestValidator = flightRequestValidator;
    }

//      Admin
public synchronized FlightResponse saveFlight(FlightRequest flightRequest) {
    flightRequestValidator.validateFlightRequest(flightRequest);

    LocalDateTime departureDateTime = CustomFormater.formatStringToDateTime(flightRequest.getDepartureTime());
    LocalDateTime arrivalDateTime = CustomFormater.formatStringToDateTime(flightRequest.getArrivalTime());

    int endingId = flightPlannerRepository.getFlights().stream()
            .mapToInt(Flight::getId)
            .max()
            .orElse(0);
    int newFlightId = endingId + 1;

    Flight newFlight = new Flight(
            flightRequest.getFrom(),
            flightRequest.getTo(),
            flightRequest.getCarrier(),
            departureDateTime,
            arrivalDateTime,
            newFlightId
    );
//  Check for existing flight.
    Optional<Flight> existingFlight = flightPlannerRepository.findFlightById(newFlightId);
    if (existingFlight.isPresent()) {
        throw new ResponseStatusException(HttpStatus.CONFLICT, "Flight with the same ID already exists");
    }

    return flightPlannerRepository.saveFlight(newFlight);
}

    public synchronized String deleteFlight(String flightId) {
        Optional<Flight> existingFlight = flightPlannerRepository.findFlightById(Integer.parseInt(flightId));
        if (existingFlight.isPresent()) {
            flightPlannerRepository.deleteFlight(flightId);
            return "Flight with ID " + flightId + " has been deleted.";
        } else {
            throw new ResponseStatusException(HttpStatus.OK, "Flight with ID " + flightId + " not found.");
        }
    }
    public synchronized Optional<Flight> findFlightById(Integer id) {
        return flightPlannerRepository.getFlights()
                .stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();
    }
}
