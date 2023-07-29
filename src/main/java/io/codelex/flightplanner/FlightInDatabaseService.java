package io.codelex.flightplanner;

import io.codelex.flightplanner.configuration.FlightValidator;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FlightInDatabaseService implements FlightService {

    private final FlightInDatabaseRepository flightInDatabaseRepository;
    private final AirportInDatabaseRepository airportInDatabaseRepository;
    private final FlightValidator flightValidator;

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public FlightInDatabaseService(FlightInDatabaseRepository flightInDatabaseRepository, AirportInDatabaseRepository airportInDatabaseRepository, FlightValidator flightValidator){
        this.flightInDatabaseRepository = flightInDatabaseRepository;
        this.airportInDatabaseRepository= airportInDatabaseRepository;
        this.flightValidator = flightValidator;
    }

    @Override
    public synchronized FlightResponse saveFlight(FlightRequest flightRequest) {
        flightValidator.validateFlight(flightRequest);

        Airport fromAirport = flightRequest.getFrom();
        airportInDatabaseRepository.save(fromAirport);

        Airport toAirport = flightRequest.getTo();
        airportInDatabaseRepository.save(toAirport);

        flightValidator.validateExistingFlightInDatabase(flightRequest);

        Flight newFlight = new Flight(
                fromAirport,
                toAirport,
                flightRequest.getCarrier(),
                LocalDateTime.parse(flightRequest.getDepartureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                LocalDateTime.parse(flightRequest.getArrivalTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")),
                null
        );

        Flight savedFlight = flightInDatabaseRepository.save(newFlight);

        return new FlightResponse(
                fromAirport,
                toAirport,
                savedFlight.getCarrier(),
                savedFlight.getDepartureTime().format(timeFormatter),
                savedFlight.getArrivalTime().format(timeFormatter),
                savedFlight.getId()
        );
    }

    @Override
    public synchronized void deleteFlight(Integer flightId) {
        Optional<Flight> existingFlight = flightInDatabaseRepository.findById(flightId);
        if (existingFlight.isPresent()) {
            flightInDatabaseRepository.deleteById(flightId);
        } else {
            throw new ResponseStatusException(HttpStatus.OK);
        }
    }

    @Override
    public synchronized Optional<Flight> getFlightById(Integer id) {
        return flightInDatabaseRepository.findById(id);
    }

    @Override
    public void clearFlights() {
    flightInDatabaseRepository.deleteAll();
    airportInDatabaseRepository.deleteAll();
    }

    @Override
    public synchronized PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        flightValidator.validateNullFields(request);
        flightValidator.validateSameAirports(request.getFrom(), request.getTo());

        List<Flight> foundFlights = flightInDatabaseRepository.findAll()
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


    @Override
    public synchronized Optional<FlightResponse> findFlightById(Integer flightId) {
        Optional<Flight> flightOptional = flightInDatabaseRepository.findById(flightId);

        return Optional.ofNullable(flightOptional.map(flight -> new FlightResponse(
                flight.getFrom(),
                flight.getTo(),
                flight.getCarrier(),
                flight.getDepartureTime().format(timeFormatter),
                flight.getArrivalTime().format(timeFormatter),
                flight.getId()
        )).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @Override
    public synchronized List<Airport> searchAirports(String search) {
        String formatSearch = "%" + search.trim().toLowerCase() + "%";
        return airportInDatabaseRepository.searchByString(formatSearch);
    }
}
