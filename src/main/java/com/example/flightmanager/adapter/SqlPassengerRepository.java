package com.example.flightmanager.adapter;

import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SqlPassengerRepository extends PassengerRepository, JpaRepository<Passenger, Integer> {
}
