package com.example.flightmanager.dto;

import com.example.flightmanager.model.Passenger;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
public record PassengerDTO(
        int id,
        @NotBlank(message = "Name cannot be an empty field.")
        String name,
        @NotBlank(message = "Surname cannot be an empty field.")
        String surname,
        @NotBlank(message = "Phone cannot be an empty field.")
        String phone) {
}