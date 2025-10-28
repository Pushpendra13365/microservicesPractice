package com.auth_service.service;

import com.auth_service.config.ResponseConfig;
import com.auth_service.dto.LoginRequest;
import com.auth_service.dto.SignupRequest;
import com.auth_service.entity.TokenType;
import com.auth_service.entity.User;
import com.auth_service.entity.UserToken;
import com.auth_service.exception.CustomUnauthorizedException;
import com.auth_service.repository.UserRepository;
import com.auth_service.repository.UserTokenRepository;
import com.auth_service.utility.CookieUtil;
import com.auth_service.utility.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepo;
    private final UserTokenRepository tokenRepo;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User createUser(SignupRequest signupRequest) {
        if (userRepo.existsByUsername(signupRequest.getUsername())) {
            throw new RuntimeException("Error: Username is already taken!");
        }

        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setRole(signupRequest.getRole());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        return userRepo.save(user);
    }

    @Transactional
    public ResponseEntity<Object> login(LoginRequest request, HttpServletResponse response) {
        try {
            User user = userRepo.findByUsername(request.getUserName())
                    .orElseThrow(() -> new BadCredentialsException("Invalid username"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Invalid password");
            }

            List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
            String accessToken = jwtUtil.generateAccessToken(user.getUsername(), authorities);

            tokenRepo.invalidateAllUserTokens(user.getId());

            List<UserToken> userTokens = tokenRepo.saveAll(List.of(
                    UserToken.builder()
                            .user(user)
                            .token(accessToken)
                            .tokenType(TokenType.ACCESS)
                            .expiryTime(LocalDateTime.now().plusHours(3))
                            .build()));
            CookieUtil.addJwtCookie(response, accessToken);

            log.info("User '{}' logged in successfully", user.getUsername());
            return ResponseConfig.success("Login successful", accessToken);
        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for user: {}", request.getUserName());
            throw new CustomUnauthorizedException("Invalid username or password");
        }
    }

    @Transactional
    public Long logout(String token, HttpServletResponse response) {
        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Authorization header is missing or invalid");
        }

        String tokenValue = token.substring(7).trim();

        if (tokenValue.isEmpty()) {
            throw new CustomUnauthorizedException("Token must not be empty");
        }

        Long userId = tokenRepo.findUserIdByToken(tokenValue)
                .orElseThrow(() -> new CustomUnauthorizedException("Invalid token or already logged out"));

        int updated = tokenRepo.invalidateToken(tokenValue);

        if (updated == 0) {
            log.warn("Token not found or already invalidated: {}", tokenValue);
            throw new CustomUnauthorizedException("Token not found or already invalidated");
        }

        CookieUtil.clearJwtCookie(response);

        log.info("User with ID [{}] successfully logged out. Token invalidated.", userId);
        return userId;
    }

    public Optional<User> getUserById(Long id) {

        return userRepo.findById(id);
    }
}