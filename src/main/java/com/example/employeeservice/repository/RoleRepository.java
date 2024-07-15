
package com.example.employeeservice.repository;

import com.example.employeeservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    void deleteRoleById(Long id);

    Optional<Role> findByName(String Name);
}
