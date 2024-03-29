package io.codelex.flightplanner.request;

import io.codelex.flightplanner.domain.Airport;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

public class FlightRequest {
    @Valid
    @NotNull
    private Airport from;
    @Valid
    @NotNull
    private Airport to;
    @NotNull
    @NotEmpty
    private String carrier;
    @NotNull
    @NotEmpty
    private String departureTime;
    @NotNull
    @NotEmpty
    private String arrivalTime;

    public FlightRequest(Airport from, Airport to, String carrier, String departureTime, String arrivalTime) {
            this.from = from;
            this.to = to;
            this.carrier = carrier;
            this.departureTime = departureTime;
            this.arrivalTime = arrivalTime;
        }

    public FlightRequest() {
    }

    public Airport getFrom() {
            return from;
        }

        public void setFrom(Airport from) {
            this.from = from;
        }

        public Airport getTo() {
            return to;
        }

        public void setTo(Airport to) {
            this.to = to;
        }

        public String getCarrier() {
            return carrier;
        }

        public void setCarrier(String carrier) {
            this.carrier = carrier;
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public void setDepartureTime(String departureTime) {
            this.departureTime = departureTime;
        }

        public String getArrivalTime() {
            return arrivalTime;
        }

        public void setArrivalTime(String arrivalTime) {
            this.arrivalTime = arrivalTime;
        }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FlightRequest that = (FlightRequest) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(carrier, that.carrier) && Objects.equals(departureTime, that.departureTime) && Objects.equals(arrivalTime, that.arrivalTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, carrier, departureTime, arrivalTime);
    }

    @Override
        public String toString() {
            return "AddFlightRequest{" +
                    "from=" + from +
                    ", to=" + to +
                    ", carrier='" + carrier + '\'' +
                    ", departureTime='" + departureTime + '\'' +
                    ", arrivalTime='" + arrivalTime + '\'' +
                    '}';
        }
}
