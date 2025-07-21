package com.weather.forecast.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleGenericException(Exception ex) {
        return Map.of("error", "An unexpected error occurred");
    }

    @ExceptionHandler(CityNotFoundException.class)
    public ResponseEntity<String> handleCityNotFound(CityNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Enter a valid city name.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleEmptyCity(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Please enter a city.");
    }
}
