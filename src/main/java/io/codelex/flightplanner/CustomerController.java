package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.errorHandling.FlightNotFoundException;
import io.codelex.flightplanner.response.FlightResponse;
import io.codelex.flightplanner.response.PageResult;
import io.codelex.flightplanner.request.SearchFlightsRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerController {
    private final FlightService flightService;

    public CustomerController(FlightService flightService) {
        this.flightService = flightService;
    }


    @PostMapping("/flights/search")
    public @ResponseBody PageResult<Flight> searchFlights(@Valid @RequestBody SearchFlightsRequest request) {
        return flightService.searchFlights(request);
    }

    @GetMapping("/flights/{flightId}")
    public FlightResponse findFlightById(@PathVariable("flightId") Integer id) {
        return flightService.findFlightById(id)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found for ID: " + id));
    }

    @GetMapping("/airports")
    @ResponseStatus(HttpStatus.OK)
    public List<Airport> searchAirports(@RequestParam String search) {
        return flightService.searchAirports(search);
    }
}

