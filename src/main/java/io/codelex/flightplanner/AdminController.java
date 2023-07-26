package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Flight;
import io.codelex.flightplanner.request.FlightRequest;
import io.codelex.flightplanner.response.FlightResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/admin-api")
public class AdminController {
    private final FlightService flightService;

    public AdminController(FlightService flightService) {
        this.flightService = flightService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse saveFlight(@Valid @RequestBody FlightRequest flight){
       return flightService.saveFlight(flight);
    }
    @GetMapping("/flights/{flightId}")
    public Flight fetchFlight(@PathVariable("flightId") Integer id) {
        return flightService.getFlightById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/flights/{flightId}")
    public void deleteFlight(@PathVariable("flightId") Integer id) {
        flightService.deleteFlight(id);
    }
}
