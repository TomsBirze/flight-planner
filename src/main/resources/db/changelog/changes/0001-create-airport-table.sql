--liquibase formatted sql

--changeset toms:1

CREATE TABLE airport (
                         airport VARCHAR(3) PRIMARY KEY,
                         country VARCHAR(100),
                         city VARCHAR(100)
);