package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student registerStudent(Student student) {
        log.info("Attempting to register student with email: {}", student.getEmail());

        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            log.warn("Registration failed - email already exists: {}", student.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setId(null);

        Student saved = studentRepository.save(student);
        log.info("Student registered successfully with email: {}", saved.getEmail());

        return saved;
    }
}
