package com.example.employeeservice.controller;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/register")
    public void registerEmployee(
            @RequestBody Employee employee,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String jwtToken = authorizationHeader.substring(7);
        employeeService.registerEmployee(employee, jwtToken);
    }
}
