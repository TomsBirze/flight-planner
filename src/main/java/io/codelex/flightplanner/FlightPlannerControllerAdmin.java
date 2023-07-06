package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin-api")
public class FlightPlannerControllerAdmin {
    private final FlightPlannerService flightPlannerService;

    public FlightPlannerControllerAdmin(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @PostMapping("/flights")
    public void addFlight(@RequestBody Flight flight){
        flightPlannerService.addFlight(flight);
    }
    @GetMapping("/getflights")
    public List<Flight> getFlightList() {
        return flightPlannerService.getFlightList();
    }
    @GetMapping("/flights/{id}")
    public Optional<Flight> fetchFlight(@PathVariable("id") int id) {
        return flightPlannerService.findFlightById(id);
    }
    @DeleteMapping("/flights/{id}")
    public Optional<Flight> deleteFlight(@PathVariable("id") int id) {
        return flightPlannerService.deleteFlight(id);
    }
}
