package com.example.flightmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PassengerDTO {
    private int id;
    private String name;
    private String surname;
    private String phone;
}