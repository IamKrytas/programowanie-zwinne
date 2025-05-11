package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminStudentService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;

    public Student getStudentById(String studentId) {
        return studentRepository.findById(studentId).orElseThrow();
    }

    public List<Student> getStudents(int offset, int limit) {
        PageRequest pageable = PageRequest.of(offset, limit);
        return studentRepository.findAll(pageable).getContent();
    }

    public Student createStudent(Student student) {
        student.setId(null);
        student.setPassword(passwordEncoder.encode(student.getPassword()));
        if (studentRepository.findByEmail(student.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        studentRepository.save(student);
        return student;
    }

    public Student editStudent(String studentId, Student data){
        var student = studentRepository.findById(studentId).orElseThrow();
        student.setName(data.getName());
        student.setSurname(data.getSurname());
        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            student.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        if (data.getEmail().equals(student.getEmail())) {
            student.setEmail(data.getEmail());
        } else if (studentRepository.findByEmail(data.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        } else {
            student.setEmail(data.getEmail());
        }
        student.setStationary(data.isStationary());
        studentRepository.save(student);
        return student;
    }

    public Student deleteStudent(String studentId){
        var student = studentRepository.findById(studentId).orElseThrow();
        studentRepository.delete(student);
        return student;
    }
}
