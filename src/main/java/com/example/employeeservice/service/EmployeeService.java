package com.example.employeeservice.service;

import com.example.employeeservice.client.UserServiceClient;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.model.User;
import com.example.employeeservice.repository.EmployeeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserServiceClient userServiceClient;

    public void registerEmployee(Employee employee, String jwtToken) {
        employee.setEmployeeCode(UUID.randomUUID().toString());
        User user = userServiceClient.getUserByEmail(employee.getEmail(), jwtToken);
        if (user != null && user.getEnabled()) {
            employeeRepository.save(employee);
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    public Employee updateEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }
}
