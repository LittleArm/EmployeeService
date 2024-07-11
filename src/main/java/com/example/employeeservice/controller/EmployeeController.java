package com.example.employeeservice.controller;

import com.example.employeeservice.model.Employee;
import com.example.employeeservice.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<Employee> updateEmployee(
            @RequestBody Employee employee,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String jwtToken = authorizationHeader.substring(7);
        Employee updatedEmployee = employeeService.updateEmployee(employee, jwtToken);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteEmployee(
            @RequestParam Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
