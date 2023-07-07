package io.codelex.flightplanner.flightPlannerTesting;

import io.codelex.flightplanner.FlightPlannerRepository;
import org.springframework.stereotype.Service;

@Service
public class TestingService {
    private final FlightPlannerRepository flightPlannerRepository;

    public TestingService(FlightPlannerRepository flightPlannerRepository) {
        this.flightPlannerRepository = flightPlannerRepository;
    }
    public void clearFlights() {
        flightPlannerRepository.clearFlights();
    }
}
