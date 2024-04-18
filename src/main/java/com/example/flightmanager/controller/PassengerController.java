package com.example.flightmanager.controller;

import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import com.example.flightmanager.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerRepository passengerRepository;
    private final PassengerService passengerService;


    @PostMapping
    ResponseEntity<Passenger> addNewPassenger(@RequestBody @Valid Passenger newPassenger) {
        Passenger result = passengerRepository.save(newPassenger);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<Passenger>> readAllPassengers() {
        return ResponseEntity.ok(passengerRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Passenger> findPassengerById(@PathVariable int id) {
        return ResponseEntity.ok(passengerService.getPassenger(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updatePassenger(@PathVariable int id, @RequestBody @Valid Passenger toUpdate) {
        Passenger result = passengerService.updatePassenger(id, toUpdate);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deletePassenger(@PathVariable int id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
