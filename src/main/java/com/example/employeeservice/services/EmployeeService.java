package com.example.employeeservice.services;

import com.example.employeeservice.clients.UserServiceClient;
import com.example.employeeservice.dtos.EmployeeDTO;
import com.example.employeeservice.exceptions.UserNotFoundException;
import com.example.employeeservice.models.Employee;
import com.example.employeeservice.dtos.UserDTO;
import com.example.employeeservice.repositories.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserServiceClient userServiceClient;

    public EmployeeService(EmployeeRepository employeeRepository, UserServiceClient userServiceClient) {
        this.employeeRepository = employeeRepository;
        this.userServiceClient = userServiceClient;
    }

    public EmployeeDTO registerEmployee(EmployeeDTO employeeDTO) {
        UserDTO userDTO = userServiceClient.getUserByEmail(employeeDTO.getEmail());
        if (userDTO != null && userDTO.getEnabled()) {
            Employee employee = Employee.builder()
                    .firstName(employeeDTO.getFirstName())
                    .lastName(employeeDTO.getLastName())
                    .email(employeeDTO.getEmail())
                    .jobTitle(employeeDTO.getJobTitle())
                    .dateOfBirth(employeeDTO.getDateOfBirth())
                    .imageUrl(employeeDTO.getImageUrl())
                    .employeeCode(UUID.randomUUID().toString())
                    .locked(false)
                    .enabled(true)
                    .build();
            Employee savedEmployee = employeeRepository.save(employee);
            return convertToDTO(savedEmployee);
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    public List<EmployeeDTO> findAllEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public EmployeeDTO findEmployee(String email) {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        return convertToDTO(employee);
    }

    public EmployeeDTO updateEmployee(EmployeeDTO employeeDTO) {
        UserDTO userDTO = userServiceClient.getUserByEmail(employeeDTO.getEmail());
        if (userDTO != null && userDTO.getEnabled()) {
            userDTO.setFirstName(employeeDTO.getFirstName());
            userDTO.setLastName(employeeDTO.getLastName());
            userServiceClient.updateUser(userDTO);
            Employee updatedEmployee = employeeRepository.findByEmail(employeeDTO.getEmail())
                            .orElseThrow(() -> new RuntimeException("Employee not found"));
            updatedEmployee.setDateOfBirth(employeeDTO.getDateOfBirth());
            updatedEmployee.setFirstName(employeeDTO.getFirstName());
            updatedEmployee.setLastName(employeeDTO.getLastName());
            updatedEmployee.setImageUrl(employeeDTO.getImageUrl());
            updatedEmployee.setJobTitle(employeeDTO.getJobTitle());
            employeeRepository.save(updatedEmployee);
            return convertToDTO(updatedEmployee);
        } else {
            throw new IllegalStateException("User does not exist.");
        }
    }

    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new UserNotFoundException("Employee was not found");
        }
        employeeRepository.deleteEmployeeById(id);
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        return EmployeeDTO.builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .jobTitle(employee.getJobTitle())
                .dateOfBirth(employee.getDateOfBirth())
                .imageUrl(employee.getImageUrl())
                .build();
    }
}
