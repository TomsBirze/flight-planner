package io.codelex.flightplanner;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testing-api")
public class FlightPlannerControllerTesting {
    private final FlightPlannerService flightPlannerService;

    public FlightPlannerControllerTesting(FlightPlannerService flightPlannerService) {
        this.flightPlannerService = flightPlannerService;
    }

    @DeleteMapping("/clear")
    public void clearFlights() {
        flightPlannerService.clearFlights();
    }
}
