package com.example.employeeservice.handler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

public enum ErrorCodes {

    NO_CODE(0, NOT_IMPLEMENTED, "No code"),
    USER_NOT_FOUND(305, NOT_FOUND, "Employee not found"),
    ILLEGAL_STATE(306, BAD_REQUEST, "Invalid state")
    ;

    @Getter
    private final int code;
    @Getter
    private final String description;
    @Getter
    private final HttpStatus httpStatus;

    ErrorCodes(int code, HttpStatus httpStatus, String description) {
        this.code = code;
        this.httpStatus = httpStatus;
        this.description = description;
    }
}