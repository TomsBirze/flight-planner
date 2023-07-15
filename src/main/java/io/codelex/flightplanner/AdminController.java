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
    private final FlightInMemoryService flightInMemoryService;

    public AdminController(FlightInMemoryService flightInMemoryService) {
        this.flightInMemoryService = flightInMemoryService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse saveFlight(@Valid @RequestBody FlightRequest flight){
       return flightInMemoryService.saveFlight(flight);
    }
    @GetMapping("/flights/{flightId}")
    public Flight fetchFlight(@PathVariable("flightId") Integer id) {
        return flightInMemoryService.getFlightById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/flights/{flightId}")
    public void deleteFlight(@PathVariable("flightId") Integer id) {
        flightInMemoryService.deleteFlight(id);
    }
}
