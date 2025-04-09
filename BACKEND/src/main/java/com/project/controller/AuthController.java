package com.project.controller;

import com.project.model.auth.LoginCredentials;
import com.project.model.auth.JwtTokenPair;
import com.project.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public JwtTokenPair handleLogin(@RequestBody @Valid LoginCredentials credentials) {
        return authService.login(credentials);
    }

    @PostMapping("/token/refresh")
    public String handleTokenRefresh(@RequestBody String refreshToken) {
        try {
            return authService.refreshAccessToken(refreshToken);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid refresh token");
        }
    }
}
