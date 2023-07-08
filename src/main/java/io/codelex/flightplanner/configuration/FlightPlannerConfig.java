package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.domain.Airport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class FlightPlannerConfig {

    @Bean
    public Map<String, Airport> allAirports() {
        return new HashMap<>();
    }

}
