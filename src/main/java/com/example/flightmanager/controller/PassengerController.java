package com.example.flightmanager.controller;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {

   private final PassengerRepository passengerRepository;

    @PostMapping
    ResponseEntity<Passenger> addNewPassenger(@RequestBody Passenger newPassenger) {
        Passenger result = passengerRepository.save(newPassenger);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<Passenger>> readAllPassengers() {
        return ResponseEntity.ok(passengerRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Passenger> findPassengerById(@PathVariable int id) {
        return passengerRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable int id, @RequestBody @Valid Passenger toUpdate) {
        if (!passengerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        passengerRepository.findById(id).ifPresent(passenger -> passenger.passengerUpdate(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFlight(@PathVariable int id) {
        if (!passengerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        passengerRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }
}
