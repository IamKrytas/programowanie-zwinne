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
        // Handle admin login (special case, as admin credentials are not stored in the database)
        if (credentials.getEmail().equals(adminDefaultEmail) && credentials.getPassword().equals(adminDefaultPassword)) {
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.ADMIN);
        }

        // Handle student login
        var student = studentRepository.findByEmail(credentials.getEmail());
        if (student.isPresent() && passwordEncoder.matches(credentials.getPassword(), student.get().getPassword())) {
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.STUDENT);
        }

        // Handle teacher login
        var teacher = teacherRepository.findByEmail(credentials.getEmail());
        if (teacher.isPresent() && passwordEncoder.matches(credentials.getPassword(), teacher.get().getPassword())) {
            return jwtService.generateTokenPair(credentials.getEmail(), UserRole.TEACHER);
        }

        // If no valid credentials are found, throw an exception
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    public String refreshAccessToken(String refreshToken) {
        return jwtService.refreshAccessToken(refreshToken);
    }
}
