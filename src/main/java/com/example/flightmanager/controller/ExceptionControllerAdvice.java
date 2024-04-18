package com.example.flightmanager.controller;

import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(FlightNotFoundException.class)
    ResponseEntity<String> handleFlightNotFound(FlightNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PassengerNotFoundException.class)
    ResponseEntity<String> handlePassengerNotFound(PassengerNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(NoAvailableSeatsException.class)
    ResponseEntity<String> handleNoAvailableSeats(NoAvailableSeatsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(DuplicatePassengerException.class)
    ResponseEntity<String> handleDuplicatePassenger(DuplicatePassengerException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<Map<Integer, String>> handleValidation(MethodArgumentNotValidException e){
        Map<Integer, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String message = error.getDefaultMessage();
            errors.put(errors.size(), message);
        });
        return ResponseEntity.badRequest().body(errors);
    }
}
