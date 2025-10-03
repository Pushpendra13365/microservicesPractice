package com.auth_service.service;

import com.auth_service.config.ResponseConfig;
import com.auth_service.entity.Role;
import com.auth_service.entity.UserRole;
import com.auth_service.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;

    public Object saveUserRole(UserRole userRole) {
        try {
            if (roleRepository.findByUserRole(userRole).isPresent()) {
                return ResponseConfig.generate(HttpStatus.BAD_REQUEST, "Role already exists", null);
            }
            Role role = new Role();
            role.setUserRole(userRole);
            Role savedRole = roleRepository.save(role);
            return ResponseConfig.generate(HttpStatus.OK, "Role saved successfully", savedRole);
        } catch (Exception e) {
            log.error("Error saving role: {}", e.getMessage());
            return ResponseConfig.generate(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }

    public Object getRoleByName(UserRole userRole) {
        try {
            Optional<Role> roleOptional = roleRepository.findByUserRole(userRole);
            if (roleOptional.isPresent()) {
                return ResponseConfig.generate(HttpStatus.OK, "Success", roleOptional.get());
            } else {
                return ResponseConfig.generate(HttpStatus.NOT_FOUND, "Role not found", null);
            }
        } catch (Exception e) {
            log.error("Error fetching role: {}", e.getMessage());
            return ResponseConfig.generate(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage(), null);
        }
    }
}
