package com.project.service;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminTeacherService {
    private final TeacherRepository teacherRepository;

    public Teacher getTeacherById(String teacherId) {
        return teacherRepository.findById(teacherId).orElseThrow();
    }

    public List<Teacher> getTeachers(int offset, int limit) {
        PageRequest pageable = PageRequest.of(offset, limit);
        return teacherRepository.findAll(pageable).getContent();
    }

    public Teacher createTeacher(Teacher teacher) {
        teacher.setId(null);
        teacherRepository.save(teacher);
        return teacher;
    }

    public Teacher editTeacher(String teacherId, Teacher data){
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacher.setName(data.getName());
        teacher.setEmail(data.getEmail());
        teacher.setSurname(data.getSurname());
        teacher.setPassword(teacher.getPassword());
        teacherRepository.save(teacher);
        return teacher;
    }

    public Teacher deleteStudent(String teacherId){
        var teacher = teacherRepository.findById(teacherId).orElseThrow();
        teacherRepository.delete(teacher);
        return teacher;
    }
}
