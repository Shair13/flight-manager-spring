package com.example.flightmanager.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.domain.EntityScan;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int number;
    private String route;
    private LocalDateTime date;
    @Min(value = 0, message = "Available seats must not be less than 0")
    private int availableSeats;
    @ManyToMany
    private Set<Passenger> passengers;

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
}
