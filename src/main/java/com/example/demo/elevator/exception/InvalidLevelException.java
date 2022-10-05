package com.example.demo.elevator.exception;

import com.example.demo.elevator.exception.handler.ErrorCode;

public class InvalidLevelException extends ElevatorRestException {
    public InvalidLevelException(String message) {
        super(message, ErrorCode.BAD_REQUEST);
    }
}
