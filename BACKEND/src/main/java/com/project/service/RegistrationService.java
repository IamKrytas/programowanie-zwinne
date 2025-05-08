package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student registerStudent(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setId(null);
        return studentRepository.save(student);
    }
}


