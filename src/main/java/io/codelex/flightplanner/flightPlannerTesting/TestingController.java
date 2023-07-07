package io.codelex.flightplanner.flightPlannerTesting;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("testing-api")
public class TestingController {
    private final TestingService testingService;

    public TestingController(TestingService testingService) {
        this.testingService = testingService;
    }

    @PostMapping("/clear")
    public void clearFlights() {
        testingService.clearFlights();
    }
}
