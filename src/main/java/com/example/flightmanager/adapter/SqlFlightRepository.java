package com.example.flightmanager.adapter;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.repository.FlightRepository;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface SqlFlightRepository extends FlightRepository, JpaRepository<Flight, Integer> {
    @Override
    List<Flight> findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(String route, LocalDateTime departure, int availableSeats);
}
