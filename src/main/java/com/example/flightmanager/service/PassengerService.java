package com.example.flightmanager.service;

import com.example.flightmanager.dto.PassengerDTO;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.mapper.PassengerMapper;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PassengerService {

    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    public PassengerDTO addPassenger(PassengerDTO passengerDTO) {
        Passenger passenger = passengerMapper.dtoToEntity(passengerDTO);
        return passengerMapper.entityToDto(passengerRepository.save(passenger));
    }

    public List<PassengerDTO> readAllPassengers() {
        return passengerRepository.findAll().stream()
                .map(passengerMapper::entityToDto).toList();
    }

    public List<PassengerDTO> readAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).stream()
                .map(passengerMapper::entityToDto).toList();
    }

    public Passenger getPassenger(int id) {
        return passengerRepository.findById(id).orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found"));
    }

    public PassengerDTO getPassengerDto(int id) {
        return passengerMapper.entityToDto(passengerRepository.findById(id)
                .orElseThrow(() -> new PassengerNotFoundException("Passenger with id = " + id + " not found")));
    }

    public PassengerDTO updatePassenger(int id, Passenger toUpdate) {
        Passenger passenger = getPassenger(id);
        passenger.passengerUpdate(toUpdate);
        passengerRepository.save(passenger);
        return passengerMapper.entityToDto(passenger);
    }

    public void deletePassenger(int id) {
        passengerRepository.delete(getPassenger(id));
    }
}