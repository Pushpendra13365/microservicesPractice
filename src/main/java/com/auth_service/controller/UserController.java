package com.auth_service.controller;

import com.auth_service.config.ResponseConfig;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.LoginResponse;
import com.auth_service.dto.SignupRequest;
import com.auth_service.entity.User;
import com.auth_service.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@Valid @RequestBody SignupRequest signupRequest) {
        User user = userService.createUser(signupRequest);
        log.info("User successfully registered:{} ", +user.getId());
        return ResponseEntity.ok(user);

    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        ResponseEntity<Object> login = userService.login(request, response);
        return ResponseConfig.success("Login successful", login);

    }

    @PostMapping("/logout")
    public ResponseEntity<Object> logout(@RequestHeader("Authorization") String token, HttpServletResponse response) {
        try {
            Long userId = userService.logout(token, response);
            return ResponseConfig.success("Logout successful", userId);
        } catch (Exception e) {
            return ResponseConfig.error(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable("id") Long id) {
        Optional<User> userById = userService.getUserById(id);
        return userById.map(user -> ResponseConfig.success("User retrieved successfully", user)).orElseGet(() -> ResponseConfig.error(HttpStatus.NOT_FOUND, "User not found with id: " + id));
    }
}