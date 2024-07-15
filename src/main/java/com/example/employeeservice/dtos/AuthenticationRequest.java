package com.example.employeeservice.dtos;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AuthenticationRequest {
    private String email;
    private String password;

    // Getters and Setters
}