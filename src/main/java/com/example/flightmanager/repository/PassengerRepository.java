package com.example.flightmanager.repository;

import com.example.flightmanager.model.Passenger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PassengerRepository {
    List<Passenger> findAll();

    Page<Passenger> findAll(Pageable pageable);

    Optional<Passenger> findById(Integer id);

    Passenger save(Passenger entity);

    void delete(Passenger entity);
}
