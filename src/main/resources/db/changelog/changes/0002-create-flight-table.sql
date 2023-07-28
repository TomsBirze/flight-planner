--liquibase formatted sql

--changeset toms:2

CREATE TABLE flight (
    id SERIAL PRIMARY KEY,
    from_airport_id INT NOT NULL,
    to_airport_id INT NOT NULL,
    carrier VARCHAR(100) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    FOREIGN KEY (from_airport_id) REFERENCES airport (airport_id),
    FOREIGN KEY (to_airport_id) REFERENCES airport (airport_id)
);