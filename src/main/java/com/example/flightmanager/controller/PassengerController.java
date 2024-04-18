package com.example.flightmanager.controller;

import com.example.flightmanager.dto.PassengerDTO;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.service.PassengerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/passengers")
public class PassengerController {

    private final PassengerService passengerService;

    @PostMapping
    ResponseEntity<PassengerDTO> addNewPassenger(@RequestBody @Valid Passenger passenger) {
        PassengerDTO result = passengerService.addPassenger(passenger);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    List<PassengerDTO> readAllPassengers() {
        return passengerService.readAllPassengers();
    }

    @GetMapping
    List<PassengerDTO> readAllPassengers(Pageable page) {
        return passengerService.readAllPassengers(page);
    }

    @GetMapping("/{id}")
    PassengerDTO findPassengerById(@PathVariable int id) {
        return passengerService.getPassenger(id).passengerToDTO();
    }

    @PutMapping("/{id}")
    PassengerDTO updatePassenger(@PathVariable int id, @RequestBody @Valid Passenger toUpdate) {
        return passengerService.updatePassenger(id, toUpdate);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletePassenger(@PathVariable int id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
