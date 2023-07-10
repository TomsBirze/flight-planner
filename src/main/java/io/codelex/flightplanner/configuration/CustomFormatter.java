package io.codelex.flightplanner.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomFormatter {



    private static final String dateTimePattern = "yyyy-MM-dd HH:mm";
    private static final String datePattern = "yyyy-MM-dd";

    public static LocalDateTime formatStringToDateTime(String dateTime) {
        DateTimeFormatter departureTimeFormatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return LocalDateTime.parse(dateTime, departureTimeFormatter);
    }

    public static LocalDate formatStringToDate(String date) {
        DateTimeFormatter departureTimeFormatter = DateTimeFormatter.ofPattern(datePattern);
        return LocalDate.parse(date, departureTimeFormatter);
    }

    public static String formatLocalDateTimeToString(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateTimePattern);
        return dateTime.format(formatter);
    }

    public static String formatLocalDateToString(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return date.format(formatter);
    }
}