package com.example.flightmanager.controller;

import com.example.flightmanager.model.Passenger;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PassengerControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PassengerRepository passengerRepository;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Transactional
    void shouldAddNewPassenger() throws Exception {
        // given
        String jsonPassenger = """
                {
                	"name": "Han",
                    "surname": "Solo",
                    "phone": "123 456 789"
                }
                """;

        // when + then
        mockMvc.perform(post("/passengers").contentType("application/json").content(jsonPassenger))
                .andDo(print())
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.name", Matchers.is("Han")))
                .andExpect(jsonPath("$.surname", Matchers.is("Solo")))
                .andExpect(jsonPath("$.phone", Matchers.is("123 456 789")));
    }

    @Test
    @Transactional
    void addNewPassenger_shouldResponseBadRequest_emptyName() throws Exception {
        // given
        String jsonPassenger = """
                {
                	"name": "",
                    "surname": "Solo",
                    "phone": "123 456 789"
                }
                """;

        // when + then
        mockMvc.perform(post("/passengers").contentType("application/json").content(jsonPassenger))
                .andDo(print())
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.name", Matchers.is("Name cannot be an empty field.")));
    }

    @Test
    @Transactional
    void shouldReadAllPassengers() throws Exception {
        // given
        createPassenger("Anakin", "Skywalker", "111 111 111");
        createPassenger("Jango", "Fett", "222 222 222");

        // when + then
        MvcResult mvcResult = mockMvc.perform(get("/passengers"))
                .andExpect(status().is(200))
                .andReturn();

        Passenger[] result = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Passenger[].class);

        // then
        assertEquals(2, result.length);
    }

    @Test
    @Transactional
    void shouldFindPassengerById() throws Exception {
        // given
        Passenger passenger = createPassenger("Jango", "Fett", "222 222 222");

        // when + then
        mockMvc.perform(get("/passengers/" + passenger.getId()))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", Matchers.is(passenger.getId())))
                .andExpect(jsonPath("$.name", Matchers.is(passenger.getName())))
                .andExpect(jsonPath("$.surname", Matchers.is(passenger.getSurname())))
                .andExpect(jsonPath("$.phone", Matchers.is(passenger.getPhone())));
    }

    @Test
    @Transactional
    void findPassengerById_shouldThrowPassengerNotFoundException() throws Exception {
        // given
        int id = 13;

        // when + then
        mockMvc.perform(get("/passengers/" + id))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Passenger with id = " + id + " not found")));
    }

    @Test
    @Transactional
    void shouldUpdatePassenger() throws Exception {
        // given
        Passenger passenger = createPassenger("Jango", "Fett", "222 222 222");

        String jsonPassenger = """
                {
                	"name": "Han",
                    "surname": "Solo",
                    "phone": "123 456 789"
                }
                """;

        // when + then
        mockMvc.perform(put("/passengers/" + passenger.getId()).contentType("application/json").content(jsonPassenger))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.name", Matchers.is("Han")))
                .andExpect(jsonPath("$.surname", Matchers.is("Solo")))
                .andExpect(jsonPath("$.phone", Matchers.is("123 456 789")));
    }

    @Test
    @Transactional
    void updatePassenger_shouldThrowPassengerNotFoundException() throws Exception {
        // given
        int id = 13;

        String jsonPassenger = """
                {
                	"name": "Han",
                    "surname": "Solo",
                    "phone": "123 456 789"
                }
                """;

        // when + then
        mockMvc.perform(put("/passengers/" + id).contentType("application/json").content(jsonPassenger))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Passenger with id = " + id + " not found")));
    }

    @Test
    void shouldDeletePassenger() throws Exception {
        // given
        Passenger passenger = createPassenger("Jango", "Fett", "222 222 222");

        // when + then
        mockMvc.perform(delete("/passengers/" + passenger.getId()).contentType("application/json"))
                .andExpect(status().is(204));
    }

    @Test
    @Transactional
    void deletePassenger_shouldThrowPassengerNotFoundException() throws Exception {
        // given
        int id = 13;

        // when + then
        mockMvc.perform(delete("/passengers/" + id).contentType("application/json"))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.error", Matchers.is("Passenger with id = " + id + " not found")));
    }

    private Passenger createPassenger(String name, String surname, String phone) {
        Passenger passenger = new Passenger(name, surname, phone);
        return passengerRepository.save(passenger);
    }
}