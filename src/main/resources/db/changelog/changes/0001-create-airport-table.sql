--liquibase formatted sql

--changeset toms:1

CREATE TABLE airport (
                         country VARCHAR(100),
                         city VARCHAR(100),
                         airport VARCHAR(100) PRIMARY KEY
);