package io.codelex.flightplanner.configuration;

import io.codelex.flightplanner.request.SearchFlightsRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class SearchFlightsValidator {

    public void validateSameAirports(String fromAirport, String toAirport) {
        if (fromAirport.equalsIgnoreCase(toAirport)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request. From and to airports are the same.");
        }
    }

    public void validateNullFields(SearchFlightsRequest request) {
        if (request.getFrom() == null || request.getTo() == null || request.getDepartureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request. Missing required fields.");
        }
    }
}
