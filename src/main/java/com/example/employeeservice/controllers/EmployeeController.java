package com.example.employeeservice.controllers;

import com.example.employeeservice.model.Employee;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.employeeservice.service.EmployeeService;

import org.springframework.http.HttpHeaders;


import java.util.List;

@RestController
@RequestMapping("employee")
@Tag(name = "EmployeeManagement")
public class EmployeeController {
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
    @GetMapping("/all")
    public ResponseEntity<List<Employee>> getAllEmployees (
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        List<Employee> employees = employeeService.findAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
    @PostMapping("/register")
    public void registerEmployee(
            @RequestBody Employee employee,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String jwtToken = authorizationHeader.substring(7);
        employeeService.registerEmployee(employee, jwtToken);
    }


    @GetMapping("/find/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail (@PathVariable("email") String email) {
        Employee employee = employeeService.findEmployee(email);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }



    @PutMapping("/updatePassword")
    public ResponseEntity<Employee> updatePassword(
            @RequestBody Employee employee,
            @RequestParam String oldPassword,
            @RequestParam String newPassword
    ) {
        Employee updateEmployee = employeeService.updatePassword(employee, oldPassword, newPassword);
        return new ResponseEntity<>(updateEmployee, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("id") Long id) {
        System.out.println("meet api");
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping("/all")
//    public ResponseEntity<List<Employee>> getAllEmployees (
//            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
//    ) {
//        List<Employee> employees = employeeService.findAllEmployees();
//        return new ResponseEntity<>(employees, HttpStatus.OK);
//    }

    @PutMapping("/update")
    public ResponseEntity<Employee> updateEmployee(
            @RequestBody Employee employee,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        String jwtToken = authorizationHeader.substring(7);
        Employee updatedEmployee = employeeService.updateEmployee(employee);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteEmployee(
            @RequestParam Long id,
            @RequestHeader(HttpHeaders.AUTHORIZATION) String authorizationHeader
    ) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
