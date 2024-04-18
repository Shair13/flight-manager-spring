package com.example.flightmanager.repository;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository {
    List<Passenger> findAll();

    Optional<Passenger> findById(Integer id);

    Passenger save(Passenger entity);

    void delete(Passenger entity);
}
