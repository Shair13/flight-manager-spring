package com.example.flightmanager.exception;

public class PassengerNotFoundException extends RuntimeException {

    public PassengerNotFoundException() {
        super("Passenger not found");
    }

    public PassengerNotFoundException(String message) {
        super(message);
    }
}
