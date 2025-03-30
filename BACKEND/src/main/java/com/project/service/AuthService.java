package com.project.service;

import com.project.model.auth.LoginCredentials;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.UserRole;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final JwtTokenService jwtService;

    @Value("${app.admin.email}") private String adminDefaultEmail;
    @Value("${app.admin.password}") private String adminDefaultPassword;

    public JwtTokenPair login(LoginCredentials credentials) {
        UserRole role;

        if (credentials.getEmail().equals(adminDefaultEmail) && credentials.getPassword().equals(adminDefaultPassword)) {
            role = UserRole.ADMIN;
        } else if (studentRepository.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword()).isPresent()) {
            role = UserRole.STUDENT;
        } else if (teacherRepository.findByEmailAndPassword(credentials.getEmail(), credentials.getPassword()).isPresent()) {
            role = UserRole.TEACHER;
        } else {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }

        return jwtService.generateTokenPair(credentials.getEmail(), role);
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtService.refreshAccessToken(refreshToken);
    }
}
