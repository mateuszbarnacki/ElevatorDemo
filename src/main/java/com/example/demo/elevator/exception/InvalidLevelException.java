package com.example.demo.elevator.exception;

public class InvalidLevelException extends RuntimeException {
    public InvalidLevelException(String message) {
        super(message);
    }
}
