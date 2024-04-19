package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final PassengerService passengerService;

    @Transactional
    public FlightDTO addFlight(FlightDTO flightDTO) {
        return flightRepository.save(flightDTO.DtoToFlight()).flightToDto();
    }

    public FlightDTO addPassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        flight.checkAvailableSeats(flight);
        flight.checkDuplicatePassenger(flight, passenger);

        flight.addPassenger(passenger);
        flightRepository.save(flight);
        return flight.flightToDto();
    }

    public List<FlightDTO> readAllFlights() {
        return flightRepository.findAll().stream()
                .map(Flight::flightToDto).toList();
    }

    public List<FlightDTO> readAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable).stream()
                .map(Flight::flightToDto).toList();
    }

    @Transactional
    public FlightDTO deletePassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        flight.deletePassenger(passenger);
        flightRepository.save(flight);
        return flight.flightToDto();
    }

    @Transactional
    public FlightDTO updateFlight(int id, Flight toUpdate) {
        Flight flight = getFlight(id);
        flight.flightUpdate(toUpdate);
        flightRepository.save(flight);
        return flight.flightToDto();
    }

    @Transactional
    public void deleteFlight(int id) {
        flightRepository.delete(getFlight(id));
    }

    public Flight getFlight(int id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight with id = " + id + " not found"));
    }

    public List<FlightDTO> search(String route, LocalDateTime date, Integer availableSeats) {
        return flightRepository.findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(
                        route != null ? route : "",
                        date != null ? date : LocalDateTime.now(),
                        availableSeats != null ? availableSeats : 0).stream()
                .map(Flight::flightToDto)
                .toList();
    }
}
