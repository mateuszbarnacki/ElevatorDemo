package com.example.demo.elevator.exception.handler;

public enum ErrorCode {
    BAD_REQUEST(400), NOT_FOUND(404);

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    private final int code;
}
