package com.example.employeeservice.service;

import com.example.employeeservice.client.UserServiceClient;
import com.example.employeeservice.exception.UserNotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.model.User;
import com.example.employeeservice.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserServiceClient userServiceClient;

    public EmployeeService(EmployeeRepository employeeRepository, UserServiceClient userServiceClient) {
        this.employeeRepository = employeeRepository;
        this.userServiceClient = userServiceClient;
    }

    public void registerEmployee(Employee employee) {
        employee.setEmployeeCode(UUID.randomUUID().toString());
        User user = userServiceClient.getUserByEmail(employee.getEmail());
        if (user != null && user.getEnabled()) {
            employeeRepository.save(employee);
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    public Employee updateEmployee(Employee employee) {
        User user = userServiceClient.getUserByEmail(employee.getEmail());
        if (user != null && user.getEnabled()) {
            user.setFirstName(employee.getFirstName());
            user.setLastName(employee.getLastName());
            userServiceClient.updateUser(user);
            Employee savedEmployee = findEmployee(employee.getEmail());
            savedEmployee.setDateOfBirth(employee.getDateOfBirth());
            savedEmployee.setFirstName(employee.getFirstName());
            savedEmployee.setLastName(employee.getLastName());
            savedEmployee.setImageUrl(employee.getImageUrl());
            savedEmployee.setJobTitle(employee.getJobTitle());
            return employeeRepository.save(savedEmployee);
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }
    
    public Employee findEmployee(String email) {
        return employeeRepository.findByEmail(email)
                .orElse(null);
    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new UserNotFoundException("Employee was not found");
        }
        employeeRepository.deleteEmployeeById(id);
    }
}
