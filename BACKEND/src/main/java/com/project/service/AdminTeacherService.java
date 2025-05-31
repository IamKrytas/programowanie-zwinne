package com.project.service;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminTeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public Teacher getTeacherById(String teacherId) {
        log.info("Fetching teacher by ID: {}", teacherId);
        return teacherRepository.findById(teacherId).orElseThrow();
    }

    public List<Teacher> getTeachers(int offset, int limit) {
        log.info("Fetching list of teachers with offset {} and limit {}", offset, limit);
        PageRequest pageable = PageRequest.of(offset, limit);
        return teacherRepository.findAll(pageable).getContent();
    }

    public Teacher createTeacher(Teacher teacher) {
        log.info("Creating new teacher with email: {}", teacher.getEmail());
        teacher.setId(null);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            log.warn("Attempt to create teacher failed - email already exists: {}", teacher.getEmail());
            throw new IllegalArgumentException("Email already exists");
        }
        teacherRepository.save(teacher);
        log.info("Teacher created successfully: {}", teacher.getEmail());
        return teacher;
    }

    public Teacher editTeacher(String teacherId, Teacher data){
        log.info("Editing teacher with ID: {}", teacherId);
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setName(data.getName());
        teacher.setSurname(data.getSurname());
        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        if (data.getEmail().equals(teacher.getEmail())) {
            teacher.setEmail(data.getEmail());
        } else if (teacherRepository.findByEmail(data.getEmail()).isPresent()) {
            log.warn("Attempt to edit teacher failed - email already exists: {}", data.getEmail());
            throw new IllegalArgumentException("Email already exists");
        } else {
            teacher.setEmail(data.getEmail());
        }
        teacherRepository.save(teacher);
        log.info("Teacher with ID {} edited successfully", teacherId);
        return teacher;
    }

    public Teacher deleteTeacher(String teacherId){
        log.info("Deleting teacher with ID: {}", teacherId);
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacherRepository.delete(teacher);
        log.info("Teacher with ID {} deleted successfully", teacherId);
        return teacher;
    }
}
