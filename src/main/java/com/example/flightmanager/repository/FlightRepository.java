package com.example.flightmanager.repository;

import com.example.flightmanager.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Integer> {
}
