package com.example.flightmanager.service;

import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import com.example.flightmanager.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final PassengerRepository passengerRepository;

    public Flight addPassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = getPassenger(passengerId);

        checkAvailableSeats(flight);
        checkDuplicatePassenger(flight, passenger);

        flight.addPassenger(passenger);
        flightRepository.save(flight);
        return flight;
    }

    public Flight deletePassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = getPassenger(passengerId);

        flight.deletePassenger(passenger);
        flightRepository.save(flight);
        return flight;
    }

    public Flight updateFlight(int id, Flight toUpdate) {
        Flight flight = getFlight(id);
        flight.flightUpdate(toUpdate);
        flightRepository.save(flight);
        return flight;
    }

    public void deleteFlight(int id) {
        Flight flight = getFlight(id);
        flightRepository.delete(flight);
    }


    void checkAvailableSeats(Flight flight) {
        if (flight.getAvailableSeats() <= 0) {
            throw new NoAvailableSeatsException("No available seats on flight number " + flight.getNumber() + ".");
        }
    }

    void checkDuplicatePassenger(Flight flight, Passenger passenger) {
        if (flight.getPassengers().contains(passenger)) {
            throw new DuplicatePassengerException("Passenger with id " + passenger.getId() + " is already added to this flight");
        }
    }

    public Flight getFlight(int id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight with id = " + id + " not found"));
    }

    Passenger getPassenger(int id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
    }
}
