package io.codelex.flightplanner.flightPlannerCustomer;

import io.codelex.flightplanner.FlightPlannerService;
import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.domain.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api")
public class CustomerController {
//    private final CustomerService customerService;

//    public CustomerController(CustomerService customerService) {
//        this.customerService = customerService;
//    }
//
//    @GetMapping("/airports")
//    public List<Airport> searchAirports(@RequestParam String search) {
//        return flightPlannerService.searchAirports(search);
//    }
//
//    @PostMapping("/flights/search")
//    public PageResult<Flight> searchFlights(@RequestBody SearchFlightsRequest request) {
//        return flightPlannerService.searchFlights(request);
//    }

//    @GetMapping("/flights/{id}")
//    public Optional<Flight> findFlightById(@PathVariable int id) {
//        return flightPlannerService.findFlightById(id);
//    }
}

