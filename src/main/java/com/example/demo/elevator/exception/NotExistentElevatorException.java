package com.example.demo.elevator.exception;

public class NotExistentElevatorException extends RuntimeException {
    public NotExistentElevatorException(String message) {
        super(message);
    }
}
