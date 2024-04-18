package com.example.flightmanager.controller;

import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(FlightNotFoundException.class)
    ResponseEntity<Map<String, String>> handleFlightNotFound(FlightNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage(e.getMessage()));
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    ResponseEntity<Map<String, String>> handlePassengerNotFound(PassengerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getMessage(e.getMessage()));
    }

    @ExceptionHandler(NoAvailableSeatsException.class)
    ResponseEntity<Map<String, String>> handleNoAvailableSeats(NoAvailableSeatsException e) {
        return ResponseEntity.badRequest().body(getMessage(e.getMessage()));
    }

    @ExceptionHandler(DuplicatePassengerException.class)
    ResponseEntity<Map<String, String>> handleDuplicatePassenger(DuplicatePassengerException e) {
        return ResponseEntity.badRequest().body(getMessage(e.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<String, String>> handleValidation(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            FieldError fieldError = (FieldError) error;
            String fieldName = fieldError.getField();
            errors.put(fieldName, message);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    Map<String, String> getMessage(String errorMessage) {
        Map<String, String> message = new HashMap<>();
        message.put("error:", errorMessage);
        return message;
    }
}
