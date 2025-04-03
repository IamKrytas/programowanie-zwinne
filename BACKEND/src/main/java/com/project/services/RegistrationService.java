package com.project.services;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final StudentRepository studentRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Student registerStudent(Student student) {
        // Sprawdzenie, czy email już istnieje
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Hashowanie hasła
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        
        student.setId(null);

        // Zapis do bazy danych
        return studentRepository.save(student);
    }
}
