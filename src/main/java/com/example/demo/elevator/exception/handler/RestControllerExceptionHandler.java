package com.example.demo.elevator.exception.handler;

import com.example.demo.elevator.exception.ElevatorRestException;
import com.example.demo.elevator.exception.InvalidLevelException;
import com.example.demo.elevator.exception.NotExistentElevatorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ExceptionDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildRestException(e.getMessage(), ErrorCode.BAD_REQUEST));
    }

    @ExceptionHandler({InvalidLevelException.class})
    public ResponseEntity<ExceptionDTO> handleInvalidLevelException(InvalidLevelException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildRestException(e));
    }

    @ExceptionHandler({NotExistentElevatorException.class})
    public ResponseEntity<ExceptionDTO> handleNotExistentElevatorException(NotExistentElevatorException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(buildRestException(e));
    }

    private ExceptionDTO buildRestException(ElevatorRestException e) {
        return ExceptionDTO.builder()
                .message(e.getMessage())
                .errorCode(e.getErrorCode())
                .build();
    }

    private ExceptionDTO buildRestException(String message, ErrorCode errorCode) {
        return ExceptionDTO.builder()
                .message(message)
                .errorCode(errorCode)
                .build();
    }
}
