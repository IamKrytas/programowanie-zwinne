package com.project.service;

import com.project.model.Student;
import com.project.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.awt.print.Pageable;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final StudentRepository studentRepository;

    public Student getStudentById(String studentId) {
        var student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            return student.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Item not found");
        }
    }

    public List<Student> getStudents(int offset, int limit) {
        PageRequest pageable = PageRequest.of(offset, limit);
        return studentRepository.findAll(pageable).getContent();
    }

    public Student createStudent(Student student) {
        student.setId(null);
        studentRepository.save(student);
        return student;
    }

    public Student editStudent(String studentId, Student data){
        var student = studentRepository.findById(studentId).orElseThrow();
        student.setName(data.getName());
        student.setEmail(data.getEmail());
        student.setSurname(data.getSurname());
        student.setPassword(student.getPassword());
        student.setStationary(student.isStationary());
        studentRepository.save(student);
        return student;
    }
}
