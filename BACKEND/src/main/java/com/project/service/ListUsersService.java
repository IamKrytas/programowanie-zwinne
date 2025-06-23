package com.project.service;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListUsersService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }
}
