package com.example.flightmanager.service;

import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    public Passenger getPassenger(int id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
    }

    @Transactional
    public Passenger updatePassenger(int id, Passenger toUpdate) {
        Passenger passenger = getPassenger(id);
        passenger.passengerUpdate(toUpdate);
        passengerRepository.save(passenger);
        return passenger;
    }

    @Transactional
    public void deletePassenger(int id) {
        passengerRepository.delete(getPassenger(id));
    }

}
