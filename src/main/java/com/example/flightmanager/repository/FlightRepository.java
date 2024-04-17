package com.example.flightmanager.repository;

import com.example.flightmanager.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
    List<Flight> findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(String route, LocalDateTime date, int availableSeats);
}
