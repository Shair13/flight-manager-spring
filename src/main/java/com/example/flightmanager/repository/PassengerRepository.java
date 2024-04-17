package com.example.flightmanager.repository;

import com.example.flightmanager.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Integer> {
}
