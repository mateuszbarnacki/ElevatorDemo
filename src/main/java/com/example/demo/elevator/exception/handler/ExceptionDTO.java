package com.example.demo.elevator.exception.handler;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExceptionDTO {
    private final String message;
    private final ErrorCode errorCode;
}
