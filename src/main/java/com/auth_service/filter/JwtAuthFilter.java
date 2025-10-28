package com.auth_service.filter;

import com.auth_service.entity.User;
import com.auth_service.exception.CustomUnauthorizedException;
import com.auth_service.repository.UserRepository;
import com.auth_service.repository.UserTokenRepository;
import com.auth_service.utility.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final String AUTH_HEADER = "Authorization";
    private static final String TOKEN_PREFIX = "Bearer ";

    private final JwtUtil jwtUtil;
    private final UserRepository userRepo;
    private final UserTokenRepository tokenRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader(AUTH_HEADER);

            if (authHeader != null && authHeader.startsWith(TOKEN_PREFIX)) {
                final String token = authHeader.substring(TOKEN_PREFIX.length());

                if (tokenRepo.findByTokenAndBlacklistedFalse(token).isEmpty()) {
                    log.warn("Rejected due to blacklisted token: {}", token);
                    throw new CustomUnauthorizedException("Invalid or revoked token");
                }

                if (!jwtUtil.validateToken(token)) {
                    log.warn("Token validation failed: {}", token);
                    throw new CustomUnauthorizedException("Invalid token");
                }

                final String username = jwtUtil.extractUsername(token);
                if (username == null) {
                    log.warn("Username not found in token");
                    throw new CustomUnauthorizedException("Username missing");
                }

                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = jwtUtil.getUserDetailsFromToken(token);

                    if (userDetails == null) {
                        // Build manually
                        User user = userRepo.findByUsername(username)
                                .orElseThrow(() -> new CustomUnauthorizedException("User not found"));

                        List<SimpleGrantedAuthority> authorities = jwtUtil.extractRoles(token).stream()
                                .map(SimpleGrantedAuthority::new)
                                .collect(Collectors.toList());

                        userDetails = new org.springframework.security.core.userdetails.User(
                                user.getUsername(), "", authorities);
                    }

                    // Set authentication context
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.info("Authenticated user: {}", username);
                }
            }

            filterChain.doFilter(request, response);

        } catch (CustomUnauthorizedException e) {
            log.error("Unauthorized: {}", e.getMessage());
            SecurityContextHolder.clearContext();
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/api/auth/login") ||
                path.startsWith("/api/auth/signup") ||
                path.equals("/actuator/health");
    }
}


