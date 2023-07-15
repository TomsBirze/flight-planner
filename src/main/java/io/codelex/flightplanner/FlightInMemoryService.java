package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.FlightValidator;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightInMemoryService {
    private final FlightPlannerRepository flightPlannerRepository;
    private final FlightValidator flightValidator;
    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public FlightInMemoryService(FlightPlannerRepository flightPlannerRepository, FlightValidator flightValidator) {
        this.flightPlannerRepository = flightPlannerRepository;
        this.flightValidator = flightValidator;

    }

    public synchronized FlightResponse saveFlight(FlightRequest flightRequest) {
        flightValidator.validateFlight(flightRequest);

        int endingId = flightPlannerRepository.getFlights().stream()
            .mapToInt(Flight::getId)
            .max()
            .orElse(0);
        int newFlightId = endingId + 1;

        Flight newFlight = new Flight(
            flightRequest.getFrom(),
            flightRequest.getTo(),
            flightRequest.getCarrier(),
            LocalDateTime.parse(flightRequest.getDepartureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            LocalDateTime.parse(flightRequest.getArrivalTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
            newFlightId
        );

    flightPlannerRepository.getAirports().add(newFlight.getFrom());
    flightPlannerRepository.getAirports().add(newFlight.getTo());

    return flightPlannerRepository.saveFlight(newFlight);
}

    public synchronized void deleteFlight(Integer flightId) {
        Optional<Flight> existingFlight = getFlightById(flightId);
        if (existingFlight.isPresent()) {
            flightPlannerRepository.deleteFlight(String.valueOf(flightId));
        } else {
            throw new ResponseStatusException(HttpStatus.OK);
        }
    }

    public synchronized Optional<Flight> getFlightById(Integer id) {
        return flightPlannerRepository.getFlights()
                .stream()
                .filter(flight -> flight.getId().equals(id))
                .findFirst();
    }
    public void clearFlights() {
        flightPlannerRepository.clearFlights();
    }

//    Customer Service
public synchronized PageResult<Flight> searchFlights(SearchFlightsRequest request) {
    flightValidator.validateNullFields(request);
    flightValidator.validateSameAirports(request.getFrom(), request.getTo());

    List<Flight> foundFlights = flightPlannerRepository.getFlights()
            .stream()
            .filter(fl -> isFlightMatchingRequest(fl, request))
            .toList();

    int totalItems = foundFlights.size();
    int pageSize = 5;
    int totalPages = (int) Math.ceil((double) totalItems / pageSize);


    List<Flight> paginatedFlights = foundFlights
            .stream()
            .limit(pageSize)
            .collect(Collectors.toList());

    return new PageResult<>(totalItems, totalPages, paginatedFlights);
}

    private boolean isFlightMatchingRequest(Flight flight, SearchFlightsRequest request) {
        LocalDate requestDepartureDate = LocalDate.parse(request.getDepartureDate());
        return flight.getFrom().getAirport().equals(request.getFrom())
                && flight.getTo().getAirport().equals(request.getTo())
                && flight.getDepartureTime().toLocalDate().equals(requestDepartureDate);
    }
    public synchronized Optional<FlightResponse> findFlightById(Integer flightId) {
        Optional<Flight> flightFromDatabase = Optional.ofNullable(flightPlannerRepository
                .getFlights()
                .stream()
                .filter(flight -> Objects.equals(flight.getId(), flightId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        Flight flight = flightFromDatabase.get();


        return Optional.of(new FlightResponse(
                flight.getFrom(),
                flight.getTo(),
                flight.getCarrier(),
                flight.getDepartureTime().format(timeFormatter),
                flight.getArrivalTime().format(timeFormatter),
                flight.getId()));
    }
    public synchronized List<Airport> searchAirports(String search) {
        String formatSearch = search.trim().toLowerCase();

        return flightPlannerRepository.getAirports()
                .stream()
                .filter(airport ->
                        airport.getAirport().toLowerCase().contains(formatSearch)
                                || airport.getCity().toLowerCase().contains(formatSearch)
                                || airport.getCountry().toLowerCase().contains(formatSearch)
                )
                .map(airport -> new Airport(airport.getCountry(), airport.getCity(), airport.getAirport()))
                .collect(Collectors.toList());
    }
}
