package com.example.employeeservice.repositories;

import com.example.employeeservice.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    void deleteEmployeeByEmail(String email);

    Optional<Employee> findByEmail(String email);
}
