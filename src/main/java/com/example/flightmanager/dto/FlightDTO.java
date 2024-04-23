package com.example.flightmanager.dto;

import com.example.flightmanager.model.Passenger;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDateTime;
import java.util.Set;

public record FlightDTO(
        int id,
        @Min(value = 1, message = "Flight number must be greater than 0.")
        int number,
        @NotBlank(message = "Route cannot be an empty field.")
        String route,
        @Future(message = "Must be a future date.")
        LocalDateTime departure,
        @Min(value = 0, message = "Available seats must not be less than 0.")
        int availableSeats,
        Set<Passenger> passengers) {
}