package com.example.employeeservice.model;

import lombok.Data;

@Data
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean enabled;
    private Boolean locked;
}
