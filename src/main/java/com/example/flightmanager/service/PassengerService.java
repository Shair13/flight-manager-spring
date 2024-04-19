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
    public PassengerDTO addPassenger(PassengerDTO passengerDTO) {
        Passenger passenger = passengerRepository.save(passengerDTO.DtoToPassenger());
        return new PassengerDTO(passenger);
    }

    public List<PassengerDTO> readAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(PassengerDTO::new).toList();
    }

    public List<PassengerDTO> readAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).stream()
                .map(PassengerDTO::new).toList();
    }

    public Passenger getPassenger(int id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
    }

    public PassengerDTO getPassengerDto(int id) {
        Passenger passenger = passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
        return new PassengerDTO(passenger);
    }

    @Transactional
    public PassengerDTO updatePassenger(int id, Passenger toUpdate) {
        Passenger passenger = getPassenger(id);
        passenger.passengerUpdate(toUpdate);
        passengerRepository.save(passenger);
        return new PassengerDTO(passenger);
    }

    @Transactional
    public void deletePassenger(int id) {
        passengerRepository.delete(getPassenger(id));
    }
}