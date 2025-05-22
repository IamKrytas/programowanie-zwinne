package com.project.config;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class StartupConfig {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createTestUsers() {
        try {
            if (studentRepository.findByEmail("student@test.com").isEmpty()) {
                Student student = new Student();
                student.setName("Test");
                student.setSurname("Student");
                student.setEmail("student@test.com");
                student.setPassword(passwordEncoder.encode("test1234"));
                studentRepository.save(student);
            }

            if (teacherRepository.findByEmail("teacher@test.com").isEmpty()) {
                Teacher teacher = new Teacher();
                teacher.setName("Test");
                teacher.setSurname("Teacher");
                teacher.setEmail("teacher@test.com");
                teacher.setPassword(passwordEncoder.encode("test1234"));
                teacherRepository.save(teacher);
            }
        } catch (Exception e) {
            log.error("Error creating test users: " + e.getMessage());
        }
    }
}
