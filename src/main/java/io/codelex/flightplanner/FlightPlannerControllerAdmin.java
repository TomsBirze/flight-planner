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
public class FlightPlannerControllerAdmin {
    private final FlightPlannerService flightPlannerService;

    public FlightPlannerControllerAdmin(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PutMapping("/flights")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightResponse saveFlight(@Valid @RequestBody FlightRequest flight){
       return flightPlannerService.saveFlight(flight);
    }
    @GetMapping("/flights/{flightId}")
    public Flight fetchFlight(@PathVariable("flightId") Integer id) {
        return flightPlannerService.findFlightById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
    @DeleteMapping("/flights/{flightId}")
    public String deleteFlight(@PathVariable("flightId") String id) {
        return flightPlannerService.deleteFlight(id);
    }
}
