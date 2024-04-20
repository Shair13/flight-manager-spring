package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    private final int FLIGHT_NUMBER = 10;
    private final String FLIGHT_ROUTE = "Chicago - Warsaw";
    private final LocalDateTime DATE = LocalDateTime.now().plusDays(2);
    private int AVAILABLE_SEATS = 100;
    private int NO_AVAILABLE_SEATS = 0;
    Set<Passenger> PASSENGERS = new HashSet<>();

    @Mock
    FlightRepository mockFlightRepository;
    @Mock
    PassengerService passengerService;
    @InjectMocks
    FlightService flightService;

    @Test
    void shouldAddNewFlight() {
        // given
        FlightDTO flightDTO = new FlightDTO(1, FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, PASSENGERS);
        Flight savedFlight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, PASSENGERS);

        when(mockFlightRepository.save(flightDTO.DtoToFlight())).thenReturn(savedFlight);

        // when
        FlightDTO result = flightService.addFlight(flightDTO);

        // then
        assertEquals(FLIGHT_NUMBER, result.getNumber());
        assertEquals(FLIGHT_ROUTE, result.getRoute());
        assertEquals(DATE, result.getDate());
        assertEquals(AVAILABLE_SEATS, result.getAvailableSeats());
        assertEquals(PASSENGERS, result.getPassengers());
    }

    @Test
    void validateFlightForAddPassenger_shouldThrowNoAvailableSeatsException() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, NO_AVAILABLE_SEATS, PASSENGERS);

        // when
        NoAvailableSeatsException thrown = assertThrows(NoAvailableSeatsException.class,
                () -> flightService.validateFlightForAddPassenger(flight, null));

        // then
        assertTrue(thrown.getMessage().contains("No available seats on flight number LO"));
    }

    @Test
    void validateFlightForAddPassenger_shouldDuplicatePassengerException() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger();
        flight.addPassenger(passenger);

        // when
        DuplicatePassengerException thrown = assertThrows(DuplicatePassengerException.class,
                () -> flightService.validateFlightForAddPassenger(flight, passenger));

        // then
        assertTrue(thrown.getMessage().contains("is already added to flight number LO"));
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
        assertEquals(result.size(), 2);
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
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, passengers);
        Passenger passenger = new Passenger();
        passengers.add(passenger);
        Flight toUpdate = new Flight(20, "Roma - Stokholm", DATE.plusDays(1), 120, passengers);
        when(mockFlightRepository.findById(id)).thenReturn(Optional.of(flight));

        // when
        FlightDTO result = flightService.updateFlight(id, toUpdate);

        // then
        assertEquals(result.getNumber(), toUpdate.getNumber());
        assertEquals(result.getRoute(), toUpdate.getRoute());
        assertEquals(result.getDate(), toUpdate.getDate());
        assertEquals(result.getAvailableSeats(), toUpdate.getAvailableSeats());
        assertEquals(result.getPassengers().size(), toUpdate.getPassengers().size());
    }

    @Test
    void updateFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 13;
        Flight toUpdate = new Flight(20, "Roma - Stockholm", null, 120, null);

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class,
                () -> flightService.updateFlight(id, toUpdate));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + id + " not found"));
    }

    @Test
    void shouldAddPassengerToFlight() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger("Jan", "Nowak", "111 222 333");

        // when
        flight.addPassenger(passenger);

        // then
        assertTrue(flight.getPassengers().contains(passenger));
        assertEquals(flight.getAvailableSeats(), AVAILABLE_SEATS - 1);
        assertEquals(flight.getPassengers().size(), 1);
    }

    @Test
    void deletePassengerFromFlight_shouldThrowFlightNotFoundException() {
        // given
        int flightId = 1;

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class,
                () -> flightService.deletePassenger(flightId, anyInt()));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + flightId + " not found"));
    }

    @Test
    void shouldDeletePassengerFromFlight() {
        // given
        int flightId = 1;
        int passengerId = 3;
        Set<Passenger> passengers = new HashSet<>();
        Passenger passenger = new Passenger();
        passengers.add(passenger);
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DATE, AVAILABLE_SEATS, passengers);
        when(mockFlightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(passengerService.getPassenger(passengerId)).thenReturn(passenger);

        // when
        flightService.deletePassenger(flightId, passengerId);

        // then
        assertThat(flight.getPassengers()).isEmpty();
        assertEquals(flight.getAvailableSeats(), AVAILABLE_SEATS + 1);
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
        assertEquals(flight, result);
    }

    @Test
    void getFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 15;

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class, () -> flightService.getFlight(id));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + id + " not found"));
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
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlight(flightId));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + flightId + " not found"));
    }
}