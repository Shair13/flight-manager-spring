package com.example.flightmanager.repository;

import com.example.flightmanager.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {

    List<Flight> findAll();

    Optional<Flight> findById(Integer id);

    Flight save(Flight entity);

    void delete(Flight entity);

    List<Flight> findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(String route, LocalDateTime date, int availableSeats);
}
