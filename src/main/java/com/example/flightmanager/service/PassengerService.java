package com.example.flightmanager.service;

import com.example.flightmanager.dto.PassengerDTO;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;

    @Transactional
    public PassengerDTO addPassenger(Passenger passenger) {
        return passengerRepository.save(passenger).passengerToDTO();
    }

    public List<PassengerDTO> readAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(Passenger::passengerToDTO).toList();
    }

    public List<PassengerDTO> readAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).stream()
                .map(Passenger::passengerToDTO).toList();
    }

    public Passenger getPassenger(int id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
    }

    @Transactional
    public PassengerDTO updatePassenger(int id, Passenger toUpdate) {
        Passenger passenger = getPassenger(id);
        passenger.passengerUpdate(toUpdate);
        passengerRepository.save(passenger);
        return passenger.passengerToDTO();
    }

    @Transactional
    public void deletePassenger(int id) {
        passengerRepository.delete(getPassenger(id));
    }

}
