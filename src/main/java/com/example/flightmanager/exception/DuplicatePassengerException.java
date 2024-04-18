package com.example.flightmanager.exception;

public class DuplicatePassengerException extends RuntimeException {
    public DuplicatePassengerException() {
        super("Cannot add same passenger");
    }

    public DuplicatePassengerException(String message) {
        super(message);
    }
}
