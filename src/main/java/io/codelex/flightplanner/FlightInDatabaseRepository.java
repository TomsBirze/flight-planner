package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;


public interface FlightInDatabaseRepository extends JpaRepository<Flight, Integer> {

}
