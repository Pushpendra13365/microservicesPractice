package com.auth_service.repository;

import com.auth_service.entity.Role;
import com.auth_service.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
    Optional<Role> findByUserRole(UserRole userRole);
}
