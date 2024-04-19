package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {

    @Mock
    FlightRepository mockFlightRepository;
    @Mock
    PassengerService passengerService;
    @InjectMocks
    FlightService flightService;

    @Test
    void shouldAddNewFlight() {
        // given
        LocalDateTime date = LocalDateTime.now();
        Flight flight = new Flight();
        Flight savedFlight = new Flight(13, "Warsaw - London", date, 13, null);
        when(mockFlightRepository.save(flight)).thenReturn(savedFlight);
        FlightDTO flightDTO = mock(FlightDTO.class);
        when(flightDTO.DtoToFlight()).thenReturn(flight);
        // when
        FlightDTO result = flightService.addFlight(flightDTO);
        //then
        assertThat(result.getNumber()).isEqualTo(savedFlight.getNumber());
        assertThat(result.getRoute()).isEqualTo(savedFlight.getRoute());
        assertThat(result.getDate()).isEqualTo(savedFlight.getDate());
        assertThat(result.getAvailableSeats()).isEqualTo(savedFlight.getAvailableSeats());
        assertThat(result.getPassengers()).isEqualTo(savedFlight.getPassengers());
    }

    @Test
    void validateFlightForAddPassenger_shouldThrowNoAvailableSeatsException() {
        // given
        Flight flight = new Flight(10, "Chicago - Warsaw", null, 0, null);
        // when
        var exception = catchThrowable(() -> flightService.validateFlightForAddPassenger(flight, null));
        // then
        assertThat(exception).isInstanceOf(NoAvailableSeatsException.class).hasMessageContaining("No available seats on flight number LO");
    }

    @Test
    void validateFlightForAddPassenger_shouldDuplicatePassengerException() {
        // given
        int availableSeats = 10;
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(10, "Chicago - Warsaw", null, availableSeats, passengers);
        Passenger passenger = new Passenger();
        flight.addPassenger(passenger);
        // when
        var exception = catchThrowable(() -> flightService.validateFlightForAddPassenger(flight, passenger));
        // then
        assertThat(exception).isInstanceOf(DuplicatePassengerException.class).hasMessageContaining("is already added to flight number LO");
        assertThat(flight.getAvailableSeats()).isEqualTo(availableSeats - 1);
    }

    @Test
    void shouldReadAllPassengers() {
        // given
        Flight flightOne = new Flight();
        Flight flightTwo = new Flight();
        List<Flight> flights = List.of(flightOne, flightTwo);
        when(mockFlightRepository.findAll()).thenReturn(flights);
        // when
        List<FlightDTO> result = flightService.readAllFlights();
        // then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void shouldReadAllPassengers_emptyListOfFlights() {
        // given
        // when
        List<FlightDTO> result = flightService.readAllFlights();
        // then
        assertThat(result).isEmpty();
    }

    @Test
    void shouldUpdateFlight() {
        // given
        int id = 13;
        LocalDateTime date = LocalDateTime.now();
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(13, "Paris - Warsaw", date, 130, passengers);
        Passenger passenger = new Passenger();
        passengers.add(passenger);
        Flight toUpdate = new Flight(20, "Roma - Stokholm", date.plusDays(1), 120, passengers);
        when(mockFlightRepository.findById(id)).thenReturn(Optional.of(flight));
        // when
        FlightDTO result = flightService.updateFlight(id, toUpdate);
        // then
        assertThat(result.getNumber()).isEqualTo(toUpdate.getNumber());
        assertThat(result.getRoute()).isEqualTo(toUpdate.getRoute());
        assertThat(result.getDate()).isEqualTo(toUpdate.getDate());
        assertThat(result.getAvailableSeats()).isEqualTo(toUpdate.getAvailableSeats());
        assertThat(result.getPassengers().size()).isEqualTo(toUpdate.getPassengers().size());
    }

    @Test
    void updateFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 13;
        Flight toUpdate = new Flight(20, "Roma - Stockholm", null, 120, null);
        // when
        var exception = catchThrowable(() -> flightService.updateFlight(id, toUpdate));
        // then
        assertThat(exception).isInstanceOf(FlightNotFoundException.class).hasMessageContaining("Flight with id = " + id + " not found");
    }

    @Test
    void shouldAddPassengerToFlight() {
        // given
        LocalDateTime date = LocalDateTime.now().plusDays(2);
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(13, "Chicago - Warsaw", date, 100, passengers);
        Passenger passenger = new Passenger("Jan", "Nowak", "111 222 333");
        // when
        flight.addPassenger(passenger);
        // then
        assertThat(flight.getPassengers()).contains(passenger);
        assertThat(flight.getAvailableSeats()).isEqualTo(99);
    }

    @Test
    void deletePassengerFromFlight_shouldThrowFlightNotFoundException() {
        // given
        int flightId = 1;
        int passengerId = 3;
        // when
        var exception = catchThrowable(() -> flightService.deletePassenger(flightId, passengerId));
        // then
        assertThat(exception).isInstanceOf(FlightNotFoundException.class).hasMessageContaining("Flight with id = 1 not found");
    }

    @Test
    void shouldDeletePassengerFromFlight() {
        // given
        int flightId = 1;
        int passengerId = 3;
        Passenger passenger = new Passenger();
        Set<Passenger> passengers = new HashSet<>();
        passengers.add(passenger);
        Flight flight = new Flight(13, "Gdańsk - Berlin", null, 100, passengers);
        when(mockFlightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(passengerService.getPassenger(passengerId)).thenReturn(passenger);
        // when
        flightService.deletePassenger(flightId, passengerId);
        // then
        assertThat(flight.getPassengers()).isEmpty();
        assertThat(flight.getAvailableSeats()).isEqualTo(101);
    }

    @Test
    void shouldGetFlight() {
        // given
        int id = 15;
        Flight flight = new Flight();
        when(mockFlightRepository.findById(id)).thenReturn(Optional.of(flight));
        // when
        Flight result = flightService.getFlight(15);
        // then
        assertThat(result).isSameAs(flight);
    }

    @Test
    void getFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 15;
        Flight flight = new Flight();
        // when
        var exception = catchThrowable(() -> flightService.getFlight(15));
        // then
        assertThat(exception).isInstanceOf(FlightNotFoundException.class).hasMessageContaining("Flight with id = " + id + " not found");
    }

    @Test
    public void shouldDeleteFlightFromRepository() {
        // given
        int flightId = 1;
        Flight flight = new Flight();
        when(mockFlightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        // when
        flightService.deleteFlight(flightId);
        // then
        verify(mockFlightRepository, times(1)).delete(flight);
    }

    @Test
    public void deleteFlight_shouldThrowFlightNotFoundException() {
        // given
        int flightId = 1;
        // when
        var exception = catchThrowable(() -> flightService.deleteFlight(flightId));
        // then
        assertThat(exception).isInstanceOf(FlightNotFoundException.class).hasMessageContaining("Flight with id = 1 not found");
    }
}