package com.example.flightmanager.controller;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.service.FlightService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<FlightDTO> addNewFlight(@RequestBody @Valid FlightDTO flightDTO) {
        FlightDTO result = flightService.addFlight(flightDTO);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @GetMapping(params = {"!sort", "!page", "!size"})
    public List<FlightDTO> readAllFlights() {
        return flightService.readAllFlights();
    }

    @GetMapping
    public List<FlightDTO> readAllFlights(Pageable page) {
        return flightService.readAllFlights(page);
    }

    @GetMapping("/{id}")
    public FlightDTO findFlightById(@PathVariable int id) {
        Flight flight = flightService.getFlight(id);
        return new FlightDTO(flight);
    }

    @PutMapping("/{id}")
    public FlightDTO updateFlight(@PathVariable int id, @RequestBody @Valid Flight toUpdate) {
        return flightService.updateFlight(id, toUpdate);
    }

    @PatchMapping("/add/{flightId}/{passengerId}")
    public FlightDTO addPassengerToFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        return flightService.addPassenger(flightId, passengerId);
    }

    @PatchMapping("/delete/{flightId}/{passengerId}")
    public FlightDTO deletePassengerFromFlight(@PathVariable int flightId, @PathVariable int passengerId) {
        return flightService.deletePassenger(flightId, passengerId);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFlight(@PathVariable int id) {
        flightService.deleteFlight(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<FlightDTO> searchFlights(
            @RequestParam(required = false) String route,
            @RequestParam(required = false) LocalDateTime date,
            @RequestParam(required = false) Integer availableSeats) {
        return flightService.search(route, date, availableSeats);
    }
}
