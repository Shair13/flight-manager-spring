package com.example.flightmanager.controller;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import com.example.flightmanager.repository.PassengerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    @PostMapping
    ResponseEntity<Flight> addNewFlight(@RequestBody Flight newFlight) {
        Flight result = flightRepository.save(newFlight);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<Flight>> readAllFlights() {
        return ResponseEntity.ok(flightRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Flight> findFlightById(@PathVariable int id) {
        return flightRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable int id, @RequestBody @Valid Flight toUpdate) {
        if (!flightRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        flightRepository.findById(id).ifPresent(flight -> flight.flightUpdate(toUpdate));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFlight(@PathVariable int id) {
        if (!flightRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        flightRepository.deleteById(id);
        return ResponseEntity.ok(id);
    }

    @Transactional
    @PatchMapping("/add/{flightId}/{passengerId}")
    public ResponseEntity<?> addPassengerToFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        if (!flightRepository.existsById(flightId) || !passengerRepository.existsById(passengerId)) {
            return ResponseEntity.notFound().build();
        }
        Passenger passengerToAdd = passengerRepository.findById(passengerId).orElseThrow(RuntimeException::new);
        flightRepository.findById(flightId).map(flight -> {
            flight.getPassengers().add(passengerToAdd);
            flight.setAvailableSeats(flight.getAvailableSeats() - 1);
            return flight;
        }).orElseThrow(RuntimeException::new);
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @PatchMapping("/delete/{flightId}/{passengerId}")
    public ResponseEntity<?> removePassengerFromFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        if (!flightRepository.existsById(flightId) || !passengerRepository.existsById(passengerId)) {
            return ResponseEntity.notFound().build();
        }
        Passenger passengerToRemove = passengerRepository.findById(passengerId).orElseThrow(RuntimeException::new);
        flightRepository.findById(flightId).map(flight -> {
            flight.getPassengers().remove(passengerToRemove);
            flight.setAvailableSeats(flight.getAvailableSeats() + 1);
            return flight;
        }).orElseThrow(RuntimeException::new);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    ResponseEntity<List<Flight>> searchFlights(
            @RequestParam(required = false) String route,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Integer availableSeats){
        List<Flight> flights = flightRepository.findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(
                route != null ? route : "",
                date != null ? date : LocalDateTime.now(),
                availableSeats != null ? availableSeats : 0);
        return ResponseEntity.ok(flights);
    }
}
