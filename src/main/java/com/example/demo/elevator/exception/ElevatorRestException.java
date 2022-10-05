package com.example.demo.elevator.exception;

import com.example.demo.elevator.exception.handler.ErrorCode;

public class ElevatorRestException extends RuntimeException {
    private final ErrorCode errorCode;

    public ElevatorRestException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
