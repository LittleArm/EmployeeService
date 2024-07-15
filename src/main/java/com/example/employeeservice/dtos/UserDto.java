package com.example.employeeservice.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private Boolean enabled; // Thêm trường enabled
    private String password;
    // Các trường khác nếu cần
}
