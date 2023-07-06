package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.domain.SearchFlightsRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlightPlannerService {
    private final FlightPlannerRepository flightPlannerRepository;

    public FlightPlannerService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }
    public void clearFlights() {
        flightPlannerRepository.clearFlights();
    }
    public void addFlight(Flight flight) {
        flightPlannerRepository.addFlight(flight);
    }
    public List<Flight> getFlightList() {
        return flightPlannerRepository.getFlightList();
    }
    public Optional<Flight> findFlightById(int id) {
        return flightPlannerRepository.findFlightById(id);
    }
    public Optional<Flight> deleteFlight(int id) {
        Optional<Flight> flightToDelete = flightPlannerRepository.findFlightById(id);
        flightToDelete.ifPresent(flightPlannerRepository::removeFlight);
        return flightToDelete;
    }
    public List<Airport> searchAirports(String search) {
        return flightPlannerRepository.searchAirports(search);
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        return flightPlannerRepository.searchFlights(request);
    }
}
