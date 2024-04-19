package com.example.flightmanager.dto;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;
@Getter
@AllArgsConstructor
public class FlightDTO {
    private int id;
    @Min(value = 1, message = "Flight number must be greater than 0.")
    private int number;
    @NotBlank(message = "Route cannot be an empty field.")
    private String route;
    @Future(message = "Must be a future date.")
    private LocalDateTime date;
    @Min(value = 0, message = "Available seats must not be less than 0.")
    private int availableSeats;
    private Set<Passenger> passengers;

    public FlightDTO(Flight flight) {
        this.id = flight.getId();
        this.number = flight.getNumber();
        this.route = flight.getRoute();
        this.date = flight.getDate();
        this.availableSeats = flight.getAvailableSeats();
        this.passengers = flight.getPassengers();
    }

    public Flight DtoToFlight(){
        return new Flight(number, route, date, availableSeats, passengers);
    }
}
