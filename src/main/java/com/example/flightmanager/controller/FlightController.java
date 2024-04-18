package com.example.flightmanager.controller;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.repository.FlightRepository;
import com.example.flightmanager.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@ExceptionProcessing
@RequestMapping("/flights")
public class FlightController {

    private final FlightRepository flightRepository;
    private final FlightService flightService;

    @PostMapping
    ResponseEntity<Flight> addNewFlight(@RequestBody @Valid Flight newFlight) {
        Flight result = flightRepository.save(newFlight);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    ResponseEntity<List<Flight>> readAllFlights() {
        return ResponseEntity.ok(flightRepository.findAll());
    }

    @GetMapping("/{id}")
    ResponseEntity<Flight> findFlightById(@PathVariable int id) {
        return ResponseEntity.ok(flightService.getFlight(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateFlight(@PathVariable int id, @RequestBody @Valid Flight toUpdate) {
        Flight result = flightService.updateFlight(id, toUpdate);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/add/{flightId}/{passengerId}")
    public ResponseEntity<?> addPassengerToFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        Flight result = flightService.addPassenger(flightId, passengerId);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/delete/{flightId}/{passengerId}")
    public ResponseEntity<?> deletePassengerFromFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        Flight result = flightService.deletePassenger(flightId, passengerId);
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<?> deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    ResponseEntity<List<Flight>> searchFlights(
            @RequestParam(required = false) String route,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Integer availableSeats) {
        List<Flight> flights = flightRepository.findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(
                route != null ? route : "",
                date != null ? date : LocalDateTime.now(),
                availableSeats != null ? availableSeats : 0);
        return ResponseEntity.ok(flights);
    }
}
