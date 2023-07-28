--liquibase formatted sql

--changeset toms:1

CREATE TABLE airport (
                         airport_id SERIAL PRIMARY KEY,
                         airport VARCHAR(3) NOT NULL UNIQUE,
                         country VARCHAR(100) NOT NULL,
                         city VARCHAR(100) NOT NULL
);