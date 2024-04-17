package com.example.flightmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
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
    private int availableSeats;
    @ManyToMany
    private Set<Passenger> passengers;
    private boolean archive;

    public void flightUpdate(final Flight source){
        number = source.number;
        route = source.route;
        date = source.date;
        availableSeats = source.availableSeats;
    }
}
