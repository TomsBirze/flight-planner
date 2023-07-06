package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.domain.SearchFlightsRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class FlightPlannerControllerCustomer {
    private final FlightPlannerService flightPlannerService;

    public FlightPlannerControllerCustomer(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }
    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam("search") String search) {
        return flightPlannerService.searchAirports(search);
    }

    @PostMapping("/flights/search")
    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest request) {
        return flightPlannerService.searchFlights(request);
    }

    @GetMapping("/flights/{id}")
    public Optional<Flight> findFlightById(@PathVariable("id") int id) {
        return flightPlannerService.findFlightById(id);
    }
}

