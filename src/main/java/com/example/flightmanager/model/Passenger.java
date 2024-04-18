package com.example.flightmanager.model;

import com.example.flightmanager.dto.FlightDTO;
import com.example.flightmanager.dto.PassengerDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "passengers")
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotBlank(message = "Name cannot be an empty field.")
    private String name;
    @NotBlank(message = "Surname cannot be an empty field.")
    private String surname;
    @NotBlank(message = "Phone cannot be an empty field.")
    private String phone;

    public void passengerUpdate(final Passenger source){
        name = source.name;
        surname = source.surname;
        phone = source.phone;
    }

    public PassengerDTO passengerToDTO() {
        return new PassengerDTO(id, name, surname, phone);
    }
}
