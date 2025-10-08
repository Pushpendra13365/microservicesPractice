package com.auth_service.controller;
import com.auth_service.config.ResponseConfig;
import com.auth_service.entity.UserRole;
import com.auth_service.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping("/saveUserRole")
    public Object saveUserRole(@RequestBody String roleName) {
        try {
            UserRole userRole = UserRole.valueOf(roleName);
            return roleService.saveUserRole(userRole);
        } catch (IllegalArgumentException e) {
            return ResponseConfig.generate(HttpStatus.BAD_REQUEST, "Invalid role name. Allowed: ROLE_USER, ROLE_ADMIN, ROLE_MEDIATOR", null);
        }
    }

    @GetMapping("/getUserByName/{name}")
    public Object getUserByName(@PathVariable("name") String name) {
        try {
            UserRole userRole = UserRole.valueOf(name);
            return roleService.getRoleByName(userRole);
        } catch (IllegalArgumentException e) {
            return ResponseConfig.generate(HttpStatus.BAD_REQUEST,"Invalid role name. Allowed: ROLE_USER, ROLE_ADMIN, ROLE_MEDIATOR", null);
        }
    }
}
