package com.example.flightmanager.exception;

public class FlightNotFoundException extends RuntimeException {
    public FlightNotFoundException() {
        super("Flight not found");
    }

    public FlightNotFoundException(String message) {
        super(message);
    }
}
