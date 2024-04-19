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
    public ResponseEntity<PassengerDTO> addNewPassenger(@RequestBody @Valid PassengerDTO passengerDTO) {
        PassengerDTO result = passengerService.addPassenger(passengerDTO);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    public List<PassengerDTO> readAllPassengers() {
        return passengerService.readAllPassengers();
    }

    @GetMapping
    public List<PassengerDTO> readAllPassengers(Pageable page) {
        return passengerService.readAllPassengers(page);
    }

    @GetMapping("/{id}")
    public PassengerDTO findPassengerById(@PathVariable int id) {
        return passengerService.getPassengerDto(id);
    }

    @PutMapping("/{id}")
    public PassengerDTO updatePassenger(@PathVariable int id, @RequestBody @Valid Passenger toUpdate) {
        return passengerService.updatePassenger(id, toUpdate);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable int id) {
        passengerService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}
