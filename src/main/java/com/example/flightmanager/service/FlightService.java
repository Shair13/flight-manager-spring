package com.example.flightmanager.service;

import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FlightService {

    private final FlightRepository flightRepository;
    private final PassengerService passengerService;

    public Flight addPassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        flight.checkAvailableSeats(flight);
        flight.checkDuplicatePassenger(flight, passenger);

        flight.addPassenger(passenger);
        flightRepository.save(flight);
        return flight;
    }

    @Transactional
    public Flight deletePassenger(int flightId, int passengerId) {
        Flight flight = getFlight(flightId);
        Passenger passenger = passengerService.getPassenger(passengerId);

        flight.deletePassenger(passenger);
        flightRepository.save(flight);
        return flight;
    }

    @Transactional
    public Flight updateFlight(int id, Flight toUpdate) {
        Flight flight = getFlight(id);
        flight.flightUpdate(toUpdate);
        flightRepository.save(flight);
        return flight;
    }

    @Transactional
    public void deleteFlight(int id) {
        flightRepository.delete(getFlight(id));
    }

    public Flight getFlight(int id) {
        return flightRepository.findById(id).orElseThrow(() -> new FlightNotFoundException("Flight with id = " + id + " not found"));
    }
}
