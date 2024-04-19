package com.example.flightmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Min(value = 1, message = "Flight number must be greater than 0.")
    private int number;
    @NotBlank(message = "Route cannot be an empty field.")
    private String route;
    @Future(message = "Must be a future date.")
    private LocalDateTime date;
    @Min(value = 0, message = "Available seats must not be less than 0.")
    private int availableSeats;
    @ManyToMany
    private Set<Passenger> passengers;

    public Flight(int number, String route, LocalDateTime date, int availableSeats, Set<Passenger> passengers) {
        this.number = number;
        this.route = route;
        this.date = date;
        this.availableSeats = availableSeats;
        this.passengers = passengers;
    }

    public void flightUpdate(final Flight source) {
        number = source.number;
        route = source.route;
        date = source.date;
        availableSeats = source.availableSeats;
    }

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
        availableSeats--;
    }

    public void deletePassenger(Passenger passenger) {
        passengers.remove(passenger);
        availableSeats++;
    }

    public boolean checkAvailableSeats() {
        return availableSeats < 1;
    }

    public boolean checkDuplicatePassenger(Passenger passenger) {
        return passengers.contains(passenger);
    }
}