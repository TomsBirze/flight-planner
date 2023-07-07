package io.codelex.flightplanner.flightPlannerCustomer;

import io.codelex.flightplanner.FlightPlannerRepository;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class CustomerService {
    private final FlightPlannerRepository flightPlannerRepository;

    public CustomerService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }

    public List<Airport> searchAirports(String search) {
        return flightPlannerRepository.searchAirports(search);
    }

    public PageResult<Flight> searchFlights(SearchFlightsRequest request) {
        return flightPlannerRepository.searchFlights(request);
    }
}
