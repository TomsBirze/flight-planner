package io.codelex.flightplanner;


import io.codelex.flightplanner.domain.Airport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AirportInDatabaseRepository extends JpaRepository<Airport, String> {
    @Query("SELECT e FROM Airport e WHERE LOWER(e.airport) LIKE :search OR LOWER(e.country) LIKE :search OR LOWER(e.city) LIKE :search")
    List<Airport> searchByString(@Param("search") String search);
}