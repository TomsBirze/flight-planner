package io.codelex.flightplanner;

import io.codelex.flightplanner.domain.Airport;
import io.codelex.flightplanner.domain.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FlightInDatabaseRepository extends JpaRepository<Flight, Integer> {

    @Query("SELECT e FROM Airport e WHERE e.airport LIKE ('%' || :search || '%') OR e.country LIKE ('%' || :search || '%') OR e.city LIKE ('%' || :search || '%')")
    List<Airport> searchByString(@Param("search") String search);
}
