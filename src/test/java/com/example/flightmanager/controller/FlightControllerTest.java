package com.example.flightmanager.controller;

import com.example.flightmanager.model.Flight;
import com.example.flightmanager.model.Passenger;
import com.example.flightmanager.repository.FlightRepository;
import com.example.flightmanager.repository.PassengerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private FlightRepository flightRepository;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void shouldAddNewFlight() throws Exception {
        // given
        String jsonFlight = """
                {
                	"number": 13,
                    "route": "Warsaw - Oslo",
                    "departure": "2030-06-01T12:00:00",
                    "availableSeats": 1
                }
                """;

        // when + then
        mockMvc.perform(post("/flights").contentType("application/json").content(jsonFlight))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.number", Matchers.is(13)))
                .andExpect(jsonPath("$.route", Matchers.is("Warsaw - Oslo")))
                .andExpect(jsonPath("$.availableSeats", Matchers.is(1)));
    }

    @Test
    @Transactional
    void addNewFlight_shouldResponseBadRequest_emptyRouteAndIncorrectAvailableSeats() throws Exception {
        // given
        String jsonFlight = """
                {
                	"number": 10,
                    "route": "",
                    "departure": "2020-06-01T12:00:00",
                    "availableSeats": -1
                }
                """;

        // when + then
        mockMvc.perform(post("/flights").contentType("application/json").content(jsonFlight))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.route", Matchers.is("Route cannot be an empty field.")))
                .andExpect(jsonPath("$.availableSeats", Matchers.is("Available seats must not be less than 0.")));
    }

    @Test
    @Transactional
    void shouldReadAllFlights() throws Exception {
        // given
        createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        createEmptyFlight(15, "Warsaw - London", LocalDateTime.now().plusDays(5), 150);

        // when + then
        MvcResult mvcResult = mockMvc.perform(get("/flights"))
                .andExpect(status().is(200))
                .andReturn();

        Flight[] result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Flight[].class);

        // then
        assertEquals(2, result.length);
    }

    @Test
    @Transactional
    void shouldFindFlightById() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);

        // when + then
        mockMvc.perform(get("/flights/" + flight.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", Matchers.is(flight.getId())))
                .andExpect(jsonPath("$.number", Matchers.is(flight.getNumber())))
                .andExpect(jsonPath("$.route", Matchers.is(flight.getRoute())))
                .andExpect(jsonPath("$.availableSeats", Matchers.is(flight.getAvailableSeats())));
    }

    @Test
    void findFlightById_shouldThrowFlightNotFoundException() throws Exception {
        // given
        int id = 23;

        // when + then
        mockMvc.perform(get("/flights/" + id))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Flight with id = " + id + " not found")));
    }

    @Test
    @Transactional
    void shouldUpdateFlight() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);

        String jsonFlight = """
                {
                	"number": 15,
                    "route": "Warsaw - Oslo",
                    "departure": "2030-06-01T12:00:00",
                    "availableSeats": 15
                }
                """;

        // when + then
        mockMvc.perform(put("/flights/" + flight.getId()).contentType("application/json").content(jsonFlight))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.number", Matchers.is(15)))
                .andExpect(jsonPath("$.route", Matchers.is("Warsaw - Oslo")))
                .andExpect(jsonPath("$.availableSeats", Matchers.is(15)));
    }

    @Test
    void updateFlight_shouldThrowFlightNotFoundException() throws Exception {
        // given
        int id = 12;

        String jsonFlight = """
                {
                	"number": 15,
                    "route": "Warsaw - Oslo",
                    "departure": "2029-06-01T12:00:00",
                    "availableSeats": 15
                }
                """;

        // when + then
        mockMvc.perform(put("/flights/" + id).contentType("application/json").content(jsonFlight))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Flight with id = " + id + " not found")));
    }

    @Test
    @Transactional
    void shouldAddPassengerToFlight() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        Passenger passenger = new Passenger("Leia", "Organa", "321 321 321");
        passengerRepository.save(passenger);

        // when + then
        mockMvc.perform(patch("/flights/add/" + flight.getId() + "/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.passengers[0].name", Matchers.is(passenger.getName())))
                .andExpect(jsonPath("$.passengers[0].surname", Matchers.is(passenger.getSurname())))
                .andExpect(jsonPath("$.passengers[0].phone", Matchers.is(passenger.getPhone())))
                .andExpect(jsonPath("$.passengers.size()", Matchers.is(1)));
    }

    @Test
    void addPassengerToFlight_shouldThrowFlightNotFoundException() throws Exception {
        // given
        int flightId = 1;
        int passengerId = 1;

        // when + then
        mockMvc.perform(patch("/flights/add/" + flightId + "/" + passengerId).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Flight with id = " + flightId + " not found")));
    }

    @Test
    @Transactional
    void addPassengerToFlight_shouldThrowPassengerNotFoundException() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        int passengerId = 1;

        // when + then
        mockMvc.perform(patch("/flights/add/" + flight.getId() + "/" + passengerId).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Passenger with id = " + passengerId + " not found")));
    }

    @Test
    @Transactional
    void addPassengerToFlight_shouldThrowDuplicatePassengerException() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        Passenger passenger = new Passenger("Han", "Solo", "123 123 123");
        passengerRepository.save(passenger);
        flight.addPassenger(passenger);
        flightRepository.save(flight);

        // when + then
        mockMvc.perform(patch("/flights/add/" + flight.getId() + "/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers
                        .is("Passenger with id = " + passenger.getId() + " is already added to flight number LO" + flight.getNumber() + ".")));
    }

    @Test
    @Transactional
    void addPassengerToFlight_shouldThrowNoAvailableSeatsException() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 0);
        Passenger passenger = new Passenger("Han", "Solo", "123 123 123");
        passengerRepository.save(passenger);

        // when + then
        mockMvc.perform(patch("/flights/add/" + flight.getId() + "/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", Matchers
                        .is("No available seats on flight number LO" + flight.getNumber() + ".")));
    }

    @Test
    @Transactional
    void shouldDeletePassengerFromFlight() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        Passenger passenger = new Passenger("Han", "Solo", "123 123 123");
        passengerRepository.save(passenger);
        flight.addPassenger(passenger);
        flightRepository.save(flight);

        // when + then
        mockMvc.perform(patch("/flights/delete/" + flight.getId() + "/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.passengers.size()", Matchers.is(0)));
    }

    @Test
    @Transactional
    void deletePassengerFromFlight_shouldThrowFlightNotFoundException() throws Exception {
        // given
        int flightId = 1;
        int passengerId = 1;

        // when + then
        mockMvc.perform(patch("/flights/delete/" + flightId + "/" + passengerId).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Flight with id = " + flightId + " not found")));
    }

    @Test
    @Transactional
    void deletePassengerToFlight_shouldThrowPassengerNotFoundException_noExistingPassenger() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        int passengerId = 1;

        // when + then
        mockMvc.perform(patch("/flights/delete/" + flight.getId() + "/" + passengerId).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Passenger with id = " + passengerId + " not found")));
    }

    @Test
    @Transactional
    void deletePassengerToFlight_shouldThrowPassengerNotFoundException_existingPassengerInDatabase() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);
        Passenger passenger = new Passenger("Han", "Solo", "123 123 123");
        passengerRepository.save(passenger);

        // when + then
        mockMvc.perform(patch("/flights/delete/" + flight.getId() + "/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers
                        .is("Passenger with id = " + passenger.getId() + " not found on flight number LO" + flight.getNumber() + ".")));
    }

    @Test
    @Transactional
    void deleteFlight() throws Exception {
        // given
        Flight flight = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(2), 100);

        // when + then
        mockMvc.perform(delete("/flights/" + flight.getId()))
                .andExpect(status().is(204));
    }

    @Test
    void deleteFlight_shouldThrowFlightNotFoundException() throws Exception {
        // given
        int id = 13;

        // when + then
        mockMvc.perform(delete("/flights/" + id))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Flight with id = " + id + " not found")));
    }

    @Test
    @Transactional
    void searchFlights() throws Exception {
        // given
        Flight flightOne = createEmptyFlight(1, "Chicago - Warsaw", LocalDateTime.now().plusDays(15), 100);
        Flight flightTwo = createEmptyFlight(12, "Oslo - Berlin", LocalDateTime.now().plusDays(1), 2);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/flights/search?route=Warsaw&availableSeats=50"))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].number", Matchers.is(flightOne.getNumber())))
                .andReturn();

        Flight[] result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Flight[].class);

        // then
        assertEquals(1, result.length);
    }

    private Flight createEmptyFlight(int number, String route, LocalDateTime departure, int availableSeats) {
        Set<Passenger> passengers = new HashSet<>();
        Flight flight = new Flight(number, route, departure, availableSeats, passengers);
        return flightRepository.save(flight);
    }
}