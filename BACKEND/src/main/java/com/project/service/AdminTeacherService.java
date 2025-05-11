package com.project.service;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow();
    }

    public List<Teacher> getTeachers(int offset, int limit) {
        PageRequest pageable = PageRequest.of(offset, limit);
        return teacherRepository.findAll(pageable).getContent();
    }

    public Teacher createTeacher(Teacher teacher) {
        teacher.setId(null);
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));
        if (teacherRepository.findByEmail(teacher.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        }
        teacherRepository.save(teacher);
        return teacher;
    }

    public Teacher editTeacher(String teacherId, Teacher data){
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setName(data.getName());
        teacher.setSurname(data.getSurname());
        if (data.getPassword() != null && !data.getPassword().isEmpty()) {
            teacher.setPassword(passwordEncoder.encode(data.getPassword()));
        }
        if (data.getEmail().equals(teacher.getEmail())) {
            teacher.setEmail(data.getEmail());
        } else if (teacherRepository.findByEmail(data.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists");
        } else {
            teacher.setEmail(data.getEmail());
        }
        teacherRepository.save(teacher);
        return teacher;
    }

    public Teacher deleteTeacher(String teacherId){
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacherRepository.delete(teacher);
        return teacher;
    }
}
