package com.example.demo.elevator.exception;

import com.example.demo.elevator.exception.handler.ErrorCode;

public class NotExistentElevatorException extends ElevatorRestException {
    public NotExistentElevatorException(String message) {
        super(message, ErrorCode.NOT_FOUND);
    }
}
