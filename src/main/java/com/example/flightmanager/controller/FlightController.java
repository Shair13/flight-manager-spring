package com.example.flightmanager.controller;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.model.Flight;
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
@RequestMapping("/flights")
public class FlightController {


    private final FlightService flightService;

    @PostMapping
    ResponseEntity<FlightDTO> addNewFlight(@RequestBody @Valid Flight flight) {
        FlightDTO result = flightService.addFlight(flight);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping
    List<FlightDTO> readAllFlights() {
        return flightService.readAllFlights();
    }

    @GetMapping("/{id}")
    FlightDTO findFlightById(@PathVariable int id) {
        return flightService.getFlight(id).flightToDTO();
    }

    @PutMapping("/{id}")
    FlightDTO updateFlight(@PathVariable int id, @RequestBody @Valid Flight toUpdate) {
        return flightService.updateFlight(id, toUpdate).flightToDTO();
    }

    @PatchMapping("/add/{flightId}/{passengerId}")
    FlightDTO addPassengerToFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        return flightService.addPassenger(flightId, passengerId);
    }

    @PatchMapping("/delete/{flightId}/{passengerId}")
    FlightDTO deletePassengerFromFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        return flightService.deletePassenger(flightId, passengerId);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    List<FlightDTO> searchFlights(
            @RequestParam(required = false) String route,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Integer availableSeats) {
        return flightService.search(route, date, availableSeats);
    }
}
