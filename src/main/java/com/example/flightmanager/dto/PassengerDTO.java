package com.example.flightmanager.dto;

import com.example.flightmanager.model.Passenger;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PassengerDTO {
    private int id;
    @NotBlank(message = "Name cannot be an empty field.")
    private String name;
    @NotBlank(message = "Surname cannot be an empty field.")
    private String surname;
    @NotBlank(message = "Phone cannot be an empty field.")
    private String phone;

    public PassengerDTO(Passenger passenger) {
        this.id = passenger.getId();
        this.name = passenger.getName();
        this.surname = passenger.getSurname();
        this.phone = passenger.getPhone();
    }

    public Passenger DtoToPassenger(){
        return new Passenger(name, surname, phone);
    }
}