package com.example.employeeservice.handler;

import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

import static com.example.employeeservice.handler.ErrorCodes.*;
import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleUserNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(USER_NOT_FOUND.getCode())
                                .errorDescription(USER_NOT_FOUND.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ExceptionResponse> handleIllegalStateException(IllegalStateException exception) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .errorCode(ILLEGAL_STATE.getCode())
                                .errorDescription(ILLEGAL_STATE.getDescription())
                                .error(exception.getMessage())
                                .build()
                );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Set<String> errors = new HashSet<>();
        exception.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(
                        ExceptionResponse.builder()
                                .validationErrors(errors)
                                .build()
                );
    }

    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException exception) {
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .error(exception.getMessage())
                                .build()
                );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleException(Exception exception) {
        exception.printStackTrace();
        return ResponseEntity
                .status(INTERNAL_SERVER_ERROR)
                .body(
                        ExceptionResponse.builder()
                                .errorDescription("Internal Server Error. Please contact the admin.")
                                .error(exception.getMessage())
                                .build()
                );
    }
}