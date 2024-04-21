package com.example.flightmanager.service;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.exception.DuplicatePassengerException;
import com.example.flightmanager.exception.FlightNotFoundException;
import com.example.flightmanager.exception.NoAvailableSeatsException;
import com.example.flightmanager.exception.PassengerNotFoundException;
import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import com.example.flightmanager.repository.PassengerRepository;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightServiceTest {
    private final int FLIGHT_NUMBER = 10;
    private final String FLIGHT_ROUTE = "Chicago - Warsaw";
    private final LocalDateTime DEPARTURE = LocalDateTime.now().plusDays(2);
    private int AVAILABLE_SEATS = 100;
    private int NO_AVAILABLE_SEATS = 0;
    private Set<Passenger> PASSENGERS = new HashSet<>();

    @Mock
    private FlightRepository mockFlightRepository;
    @Mock
    private PassengerService passengerService;
    @Mock
    private PassengerRepository mockPassengerRepository;
    @InjectMocks
    private FlightService flightService;

    @Test
    void shouldAddNewFlight() {
        // given
        FlightDTO flightDTO = new FlightDTO(-1, FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);
        Flight savedFlight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);

        when(mockFlightRepository.save(flightDTO.DtoToFlight())).thenReturn(savedFlight);

        // when
        FlightDTO result = flightService.addFlight(flightDTO);

        // then
        assertEquals(FLIGHT_NUMBER, result.getNumber());
        assertEquals(FLIGHT_ROUTE, result.getRoute());
        assertEquals(DEPARTURE, result.getDeparture());
        assertEquals(AVAILABLE_SEATS, result.getAvailableSeats());
        assertEquals(PASSENGERS, result.getPassengers());
    }

    @Test
    void shouldReadAllFlights() {
        // given
        Flight flightOne = new Flight();
        Flight flightTwo = new Flight();
        List<Flight> flights = List.of(flightOne, flightTwo);
        when(mockFlightRepository.findAll()).thenReturn(flights);

        // when
        List<FlightDTO> result = flightService.readAllFlights();

        // then
        assertEquals(2, result.size());
    }

    @Test
    void shouldUpdateFlight() {
        // given
        int id = 13;
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, passengers);
        Passenger passenger = new Passenger();
        passengers.add(passenger);
        Flight toUpdate = new Flight(20, "Roma - Stokholm", DEPARTURE.plusDays(1), 120, passengers);
        when(mockFlightRepository.findById(id)).thenReturn(Optional.of(flight));

        // when
        FlightDTO result = flightService.updateFlight(id, toUpdate);

        // then
        assertEquals(toUpdate.getNumber(), result.getNumber());
        assertEquals(toUpdate.getRoute(), result.getRoute());
        assertEquals(toUpdate.getDeparture(), result.getDeparture());
        assertEquals(toUpdate.getAvailableSeats(), result.getAvailableSeats());
        assertEquals(toUpdate.getPassengers().size(), result.getPassengers().size());
    }

    @Test
    void updateFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 13;
        Flight toUpdate = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class,
                () -> flightService.updateFlight(id, toUpdate));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + id + " not found"));
    }

    @Test
    void shouldAddPassengerToFlight() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger("Jan", "Nowak", "111 222 333");

        // when
        flight.addPassenger(passenger);

        // then
        assertTrue(flight.getPassengers().contains(passenger));
        assertEquals(AVAILABLE_SEATS - 1, flight.getAvailableSeats());
        assertEquals(1, flight.getPassengers().size());
    }

    @Test
    void validateFlightForAddPassenger_shouldPass() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger();

        // when + then
        assertDoesNotThrow(() -> flightService.validateFlightForAddPassenger(flight, passenger));
    }

    @Test
    void validateFlightForAddPassenger_shouldThrowNoAvailableSeatsException() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, NO_AVAILABLE_SEATS, PASSENGERS);

        // when
        NoAvailableSeatsException thrown = assertThrows(NoAvailableSeatsException.class,
                () -> flightService.validateFlightForAddPassenger(flight, null));

        // then
        assertTrue(thrown.getMessage().contains("No available seats on flight number LO"));
    }

    @Test
    void validateFlightForAddPassenger_shouldThrowDuplicatePassengerException() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger();
        flight.addPassenger(passenger);

        // when
        DuplicatePassengerException thrown = assertThrows(DuplicatePassengerException.class,
                () -> flightService.validateFlightForAddPassenger(flight, passenger));

        // then
        assertTrue(thrown.getMessage().contains("is already added to flight number LO"));
    }

    @Test
    void shouldDeletePassengerFromFlight() {
        // given
        int flightId = 1;
        int passengerId = 3;
        Set<Passenger> passengers = new HashSet<>();
        Passenger passenger = new Passenger();
        passengers.add(passenger);
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, AVAILABLE_SEATS, passengers);
        when(mockFlightRepository.findById(flightId)).thenReturn(Optional.of(flight));
        when(passengerService.getPassenger(passengerId)).thenReturn(passenger);

        // when
        flightService.deletePassenger(flightId, passengerId);

        // then
        assertEquals(0, flight.getPassengers().size());
        assertEquals(AVAILABLE_SEATS + 1, flight.getAvailableSeats());
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
    void validateFlightForDeletePassenger_shouldPass() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, NO_AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger();
        flight.getPassengers().add(passenger);

        // when + then
        assertDoesNotThrow(() -> flightService.validateFlightForDeletePassenger(flight, passenger));
    }

    @Test
    void validateFlightForDeletePassenger_shouldThrowPassengerNotFound() {
        // given
        Flight flight = new Flight(FLIGHT_NUMBER, FLIGHT_ROUTE, DEPARTURE, NO_AVAILABLE_SEATS, PASSENGERS);
        Passenger passenger = new Passenger();

        // when
        PassengerNotFoundException thrown = assertThrows(PassengerNotFoundException.class,
                () -> flightService.validateFlightForDeletePassenger(flight, passenger));

        // then
        assertTrue(thrown.getMessage().contains("not found on flight number LO"));
    }

    @Test
    void shouldGetFlight() {
        // given
        int id = 15;
        Flight flight = new Flight();
        when(mockFlightRepository.findById(id)).thenReturn(Optional.of(flight));

        // when
        Flight result = flightService.getFlight(id);

        // then
        assertEquals(flight, result);
    }

    @Test
    void getFlight_shouldThrowFlightNotFoundException() {
        // given
        int id = 15;

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class,
                () -> flightService.getFlight(id));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + id + " not found"));
    }

    @Test
    void shouldDeleteFlightFromRepository() {
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
    void deleteFlight_shouldThrowFlightNotFoundException() {
        // given
        int flightId = 1;

        // when
        FlightNotFoundException thrown = assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlight(flightId));

        // then
        assertTrue(thrown.getMessage().contains("Flight with id = " + flightId + " not found"));
    }
}