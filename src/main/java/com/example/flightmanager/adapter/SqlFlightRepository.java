package com.example.flightmanager.adapter;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.repository.FlightRepository;
import org.springframework.data.jpa.repository.JpaRepository;

interface SqlFlightRepository extends FlightRepository, JpaRepository<Flight, Integer> {
}
