package com.example.flightmanager.repository;

import com.example.flightmanager.model.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightRepository {

    List<Flight> findAll();

    Page<Flight> findAll(Pageable pageable);

    Optional<Flight> findById(Integer id);

    Flight save(Flight entity);

    void delete(Flight entity);

    List<Flight> findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(String route, LocalDateTime date, int availableSeats);
}
