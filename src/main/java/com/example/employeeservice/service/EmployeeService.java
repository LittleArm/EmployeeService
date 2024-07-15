package com.example.employeeservice.service;

import com.example.employeeservice.handler.UserNotFoundException;
import com.example.employeeservice.model.Employee;
import com.example.employeeservice.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.employeeservice.clients.UserServiceClient;
import com.example.employeeservice.dtos.UserDto;
import java.util.List;
import java.util.UUID;
@Service
@Transactional
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    //private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserServiceClient userServiceClient;
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,UserServiceClient userServiceClient) {
        this.employeeRepository = employeeRepository;
        //this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.userServiceClient = userServiceClient;

    }

    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }
    public void registerEmployee(Employee employee, String jwtToken) {
        // Optional: Interact with user service if needed
        UserDto userDto = new UserDto();
        userDto.setEmail(employee.getEmail());
        userDto.setFirstName(employee.getFirstName());
        userDto.setLastName(employee.getLastName());
        userDto.setPassword("defaultPassord123");
        userServiceClient.registerEmployee(userDto,jwtToken);
        employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee user) {
        Employee employee = findEmployee(user.getEmail());
        employee.setFirstName(user.getFirstName());
        employee.setLastName(user.getLastName());
        employee.setDateOfBirth(user.getDateOfBirth());
        employee.setJobTitle(user.getJobTitle());
        employee.setImageUrl(user.getImageUrl());
        return employeeRepository.save(employee);
    }

    public Employee findEmployee(String email) {
        return employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Employee has email " + email + " was not found"));
    }

    public void deleteEmployee(Long id){
        if (!employeeRepository.existsById(id)){
            throw new UserNotFoundException("Employee not found with id " + id);
        }
        Employee user = employeeRepository.findById(id).orElseThrow(() -> new UserNotFoundException("Employee not found with id " + id));
        user.getRoles().clear();
        employeeRepository.save(user);
        //tokenRepository.deleteByEmployeeId(id);
        employeeRepository.deleteEmployeeById(id);
    }

    public Employee updatePassword(Employee user, String oldPassword, String newPassword) {
        Employee employee = findEmployee(user.getEmail());
        if (!checkIfValidOldPassword(employee, oldPassword)) {
            throw new IllegalStateException("Old password is incorrect");
        }
        employee.setPassword(passwordEncoder.encode(newPassword));
        return employeeRepository.save(employee);
    }

    private boolean checkIfValidOldPassword(Employee user, String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }
}
