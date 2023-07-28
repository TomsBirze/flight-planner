package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemoryConfiguration {

    @Bean
    @ConditionalOnProperty(prefix="flight", name="service.version", havingValue = "in-memory")
    public FlightService getInMemoryVersion(FlightInMemoryRepository flightInMemoryRepository, FlightValidator flightValidator) {
        return new FlightInMemoryService(flightInMemoryRepository, flightValidator);
    }
    @Bean
    @ConditionalOnProperty(prefix="flight", name="service.version", havingValue = "database")
    public FlightService getDatabaseVersion(FlightInDatabaseRepository FlightInDatabaseRepository, FlightValidator flightValidator) {
            return new FlightInDatabaseService(FlightInDatabaseRepository, flightValidator);
        }
}
