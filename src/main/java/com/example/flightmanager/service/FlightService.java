package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
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
        Flight flight = flightRepository.save(flightDTO.DtoToFlight());
        return new FlightDTO(flight);
    }

    public FlightDTO addPassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        validateFlightForAddPassenger(flight, passenger);

        flight.addPassenger(passenger);
        flightRepository.save(flight);
        return new FlightDTO(flight);
    }

    public Flight getFlight(int id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight with id = " + id + " not found"));
    }

    public List<FlightDTO> readAllFlights() {
        return flightRepository.findAll().stream()
                .map(FlightDTO::new).toList();
    }

    public List<FlightDTO> readAllFlights(Pageable pageable) {
        return flightRepository.findAll(pageable).stream()
                .map(FlightDTO::new).toList();
    }

    @Transactional
    public FlightDTO updateFlight(int id, Flight toUpdate) {
        Flight flight = getFlight(id);
        flight.flightUpdate(toUpdate);
        flightRepository.save(flight);
        return new FlightDTO(flight);
    }

    @Transactional
    public FlightDTO deletePassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        validateFlightForDeletePassenger(flight, passenger);

        flight.deletePassenger(passenger);
        flightRepository.save(flight);
        return new FlightDTO(flight);
    }

    @Transactional
    public void deleteFlight(int id) {
        flightRepository.delete(getFlight(id));
    }

    public List<FlightDTO> search(String route, LocalDateTime departure, Integer availableSeats) {
        return flightRepository.findByRouteContainingAndDateAfterAndAvailableSeatsGreaterThanEqual(
                        route != null ? route : "",
                        departure != null ? departure : LocalDateTime.now(),
                        availableSeats != null ? availableSeats : 0).stream()
                .map(FlightDTO::new)
                .toList();
    }

    void validateFlightForAddPassenger(Flight flight, Passenger passenger) {
        if (flight.checkAvailableSeats()) {
            throw new NoAvailableSeatsException("No available seats on flight number LO" + flight.getNumber() + ".");
        }
        if (flight.checkForPassengerInFlight(passenger)) {
            throw new DuplicatePassengerException("Passenger with id " + passenger.getId() + " is already added to flight number LO" + flight.getNumber() + ".");
        }
    }

    void validateFlightForDeletePassenger(Flight flight, Passenger passenger) {
        if (!flight.checkForPassengerInFlight(passenger)) {
            throw new PassengerNotFoundException("Passenger with id = " + passenger.getId() + " not found on flight number LO" + flight.getNumber() + ".");
        }
    }
}
