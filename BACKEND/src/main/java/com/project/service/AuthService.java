package com.project.service;

import com.project.model.auth.LoginCredentials;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.UserRole;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JwtTokenService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email}") private String adminDefaultEmail;
    @Value("${app.admin.password}") private String adminDefaultPassword;

    public JwtTokenPair login(LoginCredentials credentials) {
        log.info("Attempting login for email: {}", credentials.getEmail());

        // Handle admin login
        if (credentials.getEmail().equals(adminDefaultEmail) && credentials.getPassword().equals(adminDefaultPassword)) {
            log.info("Admin login successful for: {}", credentials.getEmail());
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.ADMIN);
        }

        // Handle student login
        var student = studentRepository.findByEmail(credentials.getEmail());
        if (student.isPresent() && passwordEncoder.matches(credentials.getPassword(), student.get().getPassword())) {
            log.info("Student login successful for: {}", credentials.getEmail());
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.STUDENT);
        }

        // Handle teacher login
        var teacher = teacherRepository.findByEmail(credentials.getEmail());
        if (teacher.isPresent() && passwordEncoder.matches(credentials.getPassword(), teacher.get().getPassword())) {
            log.info("Teacher login successful for: {}", credentials.getEmail());
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.TEACHER);
        }

        log.warn("Login failed for email: {}", credentials.getEmail());
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    public String refreshAccessToken(String refreshToken) {
        log.info("Refreshing access token using refresh token");
        return jwtService.refreshAccessToken(refreshToken);
    }
}

