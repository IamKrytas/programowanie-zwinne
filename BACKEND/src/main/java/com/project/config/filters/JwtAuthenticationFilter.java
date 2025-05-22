package com.project.config.filters;

import com.project.model.auth.JwtTokenType;
import com.project.service.JwtTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenService jwtService;

    @Override
    @SneakyThrows({IOException.class, ServletException.class})
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) {
        try {
            String authHeader = request.getHeader("Authorization");

            log.info("Checking if user request contains a Bearer token in Authorization header.");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                var tokenDetails = jwtService.validateToken(token);

                if (tokenDetails.getType().equals(JwtTokenType.ACCESS)) {
                    String userEmail = tokenDetails.getUserEmail();
                    String userRole = tokenDetails.getUserRole().name();

                    GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + userRole);
                    var auth = new UsernamePasswordAuthenticationToken(userEmail, null, List.of(authority));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    request.setAttribute("id", userEmail);
                    request.setAttribute("role", userRole);

                    log.info("Valid token (email: {}, role: {}).", userEmail, userRole);
                } else {
                    log.warn("Token is not of type ACCESS.");
                }
            } else {
                log.info("No Bearer token found.");
            }
        } catch (Exception e) {
            log.error("Error while processing JWT: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}

