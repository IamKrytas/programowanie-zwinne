package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminStudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student getStudentById(String studentId) {
        log.info("Fetching student by ID: {}", studentId);
        return studentRepository.findById(studentId).orElseThrow();
    }

    public List<Student> getStudents(int offset, int limit) {
        log.info("Fetching list of students with offset {} and limit {}", offset, limit);
        PageRequest pageable = PageRequest.of(offset, limit);
        return studentRepository.findAll(pageable).getContent();
    }

    public Student createStudent(Student student) {
        log.info("Creating new student with email: {}", student.getEmail());
        student.setId(null);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            log.warn("Attempt to create student failed - email already exists: {}", student.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        studentRepository.save(student);
        log.info("Student created successfully: {}", student.getEmail());
        return student;
    }

    public Student editStudent(String studentId, Student data){
        log.info("Editing student with ID: {}", studentId);
        var student = studentRepository.findById(studentId).orElseThrow();
        student.setName(data.getName());
        student.setSurname(data.getSurname());
        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        if (data.getEmail().equals(student.getEmail())) {
            student.setEmail(data.getEmail());
        } else if (studentRepository.findByEmail(data.getEmail()).isPresent()) {
            log.warn("Attempt to edit student failed - email already exists: {}", data.getEmail());
            throw new IllegalArgumentException("Email already exists");
        } else {
            student.setEmail(data.getEmail());
        }
        student.setStationary(data.isStationary());
        studentRepository.save(student);
        log.info("Student with ID {} edited successfully", studentId);
        return student;
    }

    public Student deleteStudent(String studentId){
        log.info("Deleting student with ID: {}", studentId);
        var student = studentRepository.findById(studentId).orElseThrow();
        studentRepository.delete(student);
        log.info("Student with ID {} deleted successfully", studentId);
        return student;
    }
}
