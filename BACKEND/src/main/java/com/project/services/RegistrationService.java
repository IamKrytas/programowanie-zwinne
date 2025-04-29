package com.project.services;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Student registerStudent(Student student) {
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        student.setPassword(passwordEncoder.encode(student.getPassword()));
        student.setId(null);
        return studentRepository.save(student);
    }
}

