package io.codelex.flightplanner.flightPlannerCustomer;

import io.codelex.flightplanner.FlightPlannerService;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private final CustomerService customerService;
    private final FlightPlannerService flightPlannerService;

    public CustomerController(CustomerService customerService, FlightPlannerService flightPlannerService) {
        this.customerService = customerService;
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/flights/search")
    public PageResult<Flight> searchFlights(@Valid @RequestBody SearchFlightsRequest request) {
        return customerService.searchFlights(request);
    }

    @GetMapping("/flights/{flightId}")
    public FlightResponse findFlightById(@PathVariable("flightId") Integer id) {
        return customerService.findFlightById(id);
    }
    @GetMapping("/airports")
    public List<Airport> searchAirports(@RequestParam String search) {
        return customerService.searchAirports(search);
    }
}

