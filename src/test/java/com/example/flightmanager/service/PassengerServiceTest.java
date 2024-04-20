package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.dto.PassengerDTO;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PassengerServiceTest {

    private final String NAME = "Cassian";
    private final String SURNAME = "Andor";
    private final String PHONE_NUMBER = "123 123 123";

    @Mock
    PassengerRepository mockPassengerRepository;
    @InjectMocks
    PassengerService passengerService;

    @Test
    void shouldAddPassenger() {
        // given
        PassengerDTO passengerDTO = new PassengerDTO(-1, NAME, SURNAME, PHONE_NUMBER);
        Passenger savedPassenger = new Passenger(NAME, SURNAME, PHONE_NUMBER);

        when(mockPassengerRepository.save(passengerDTO.DtoToPassenger())).thenReturn(savedPassenger);

        // when
        PassengerDTO result = passengerService.addPassenger(passengerDTO);

        // then
        assertEquals(NAME, result.getName());
        assertEquals(SURNAME, result.getSurname());
        assertEquals(PHONE_NUMBER, result.getPhone());
    }

    @Test
    void shouldReadAllPassengers() {
        // given
        Passenger passengerOne = new Passenger();
        Passenger passengerTwo = new Passenger();
        List<Passenger> passengers = List.of(passengerOne, passengerTwo);

        when(mockPassengerRepository.findAll()).thenReturn(passengers);

        // when
        List<PassengerDTO> result = passengerService.readAllPassengers();

        // then
        assertEquals(result.size(), 2);
    }

    @Test
    void shouldReadAllPassengers_emptyListOfPassengers() {
        // given

        // when
        List<PassengerDTO> result = passengerService.readAllPassengers();
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldGetPassenger() {
        // given
        int id = 10;
        Passenger passenger = new Passenger();
        when(mockPassengerRepository.findById(id)).thenReturn(Optional.of(passenger));

        // when
        Passenger result = passengerService.getPassenger(id);

        // then
        assertEquals(passenger, result);
    }

    @Test
    void getPassenger_shouldThrowPassengerNotFoundException() {
        // given
        int id = 15;

        // when
        PassengerNotFoundException thrown = assertThrows(PassengerNotFoundException.class,
                () -> passengerService.getPassenger(id));

        // then
        assertTrue(thrown.getMessage().contains("Passenger with id = " + id + " not found"));
    }

    @Test
    void shouldGetPassengerDto() {
        // given
        int id = 10;
        Passenger passenger = new Passenger(NAME, SURNAME, PHONE_NUMBER);
        when(mockPassengerRepository.findById(id)).thenReturn(Optional.of(passenger));
        // when
        PassengerDTO result = passengerService.getPassengerDto(id);

        // then
        assertEquals(NAME, result.getName());
        assertEquals(SURNAME, result.getSurname());
        assertEquals(PHONE_NUMBER, result.getPhone());
    }

    @Test
    void getPassengerDto_shouldThrowPassengerNotFoundException() {
        // given
        int id = 15;

        // when
        PassengerNotFoundException thrown = assertThrows(PassengerNotFoundException.class,
                () -> passengerService.getPassengerDto(id));

        // then
        assertTrue(thrown.getMessage().contains("Passenger with id = " + id + " not found"));
    }

    @Test
    void shouldUpdatePassenger() {
        // given
        int id = 7;
        Passenger passenger = new Passenger(NAME, SURNAME, PHONE_NUMBER);
        Passenger toUpdate = new Passenger("Jan", "Kowalski", "100 200 300");

        when(mockPassengerRepository.findById(id)).thenReturn(Optional.of(passenger));

        // when
        PassengerDTO result = passengerService.updatePassenger(id, toUpdate);

        // then
        assertEquals(toUpdate.getName(), result.getName());
        assertEquals(toUpdate.getSurname(), result.getSurname());
        assertEquals(toUpdate.getPhone(), result.getPhone());
    }

    @Test
    void updatePassenger_shouldThrowPassengerNotFoundException() {
        // given
        int id = 13;
        Passenger toUpdate = new Passenger(NAME, SURNAME, PHONE_NUMBER);

        // when
        PassengerNotFoundException thrown = assertThrows(PassengerNotFoundException.class,
                () -> passengerService.updatePassenger(id, toUpdate));

        // then
        assertTrue(thrown.getMessage().contains("Passenger with id = " + id + " not found"));
    }

    @Test
    void shouldDeletePassenger() {
        // given
        int id = 1;
        Passenger passenger = new Passenger();
        when(mockPassengerRepository.findById(id)).thenReturn(Optional.of(passenger));

        // when
        passengerService.deletePassenger(id);

        // then
        verify(mockPassengerRepository, times(1)).delete(passenger);
    }

    @Test
    public void deleteFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 1;

        // when
        PassengerNotFoundException thrown = assertThrows(PassengerNotFoundException.class, () -> passengerService.deletePassenger(id));

        // then
        assertTrue(thrown.getMessage().contains("Passenger with id = " + id + " not found"));
    }
}