package com.example.flightmanager.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
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
//    @Getter(AccessLevel.NONE)
//    @EqualsAndHashCode.Include
//    UUID uuid = UUID.randomUUID();

    public Passenger(String name, String surname, String phone) {
        this.name = name;
        this.surname = surname;
        this.phone = phone;
    }

    public void passengerUpdate(final Passenger source) {
        name = source.name;
        surname = source.surname;
        phone = source.phone;
    }
}
