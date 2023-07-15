package io.codelex.flightplanner;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testing-api")
public class TestingController {
    private final FlightInMemoryService flightInMemoryService;

    public TestingController(FlightInMemoryService flightInMemoryService) {
        this.flightInMemoryService = flightInMemoryService;
    }

    @PostMapping("/clear")
    public void clearFlights() {
        flightInMemoryService.clearFlights();
    }
}
