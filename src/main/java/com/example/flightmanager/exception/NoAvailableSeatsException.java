package com.example.flightmanager.exception;

public class NoAvailableSeatsException extends RuntimeException {
    public NoAvailableSeatsException() {
        super("No available seats on this flight.");
    }

    public NoAvailableSeatsException(String message) {
        super(message);
    }
}
