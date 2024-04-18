package com.example.flightmanager.dto;

import com.example.flightmanager.model.Passenger;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@Getter
public class FlightDTO {
    private int id;
    private int number;
    private String route;
    private LocalDateTime date;
    private int availableSeats;
    private Set<Passenger> passengers;
}
