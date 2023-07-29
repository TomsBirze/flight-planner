--liquibase formatted sql

--changeset toms:2

CREATE TABLE flight (
                        id SERIAL PRIMARY KEY,
                        from_airport VARCHAR(3),
                        to_airport VARCHAR(3),
                        carrier VARCHAR(100),
                        departure_time TIMESTAMP,
                        arrival_time TIMESTAMP,
                        FOREIGN KEY (from_airport) REFERENCES airport (airport),
                        FOREIGN KEY (to_airport) REFERENCES airport (airport)
);