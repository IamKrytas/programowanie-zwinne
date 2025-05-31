package com.project.config;

import com.project.model.Project;
import com.project.model.Student;
import com.project.model.Task;
import com.project.model.Teacher;
import com.project.repository.ProjectRepository;
import com.project.repository.StudentRepository;
import com.project.repository.TaskRepository;
import com.project.repository.TeacherRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Set;

@Configuration
@Slf4j @RequiredArgsConstructor
public class StartupConfig {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void createTestResources() {
        try {
            Student student = new Student();
            if (studentRepository.findByEmail("student@test.com").isEmpty()) {
                student.setName("Test");
                student.setSurname("Student");
                student.setEmail("student@test.com");
                student.setPassword(passwordEncoder.encode("test1234"));
                student = studentRepository.save(student);
                log.info("Test student created: {}", student);
            } else {
                student = studentRepository.findByEmail("student@test.com").orElseThrow();
                log.info("Test student already exists, skipping creation.");
            }

            Teacher teacher = new Teacher();
            if (teacherRepository.findByEmail("teacher@test.com").isEmpty()) {
                teacher.setName("Test");
                teacher.setSurname("Teacher");
                teacher.setEmail("teacher@test.com");
                teacher.setPassword(passwordEncoder.encode("test1234"));
                teacher = teacherRepository.save(teacher);
                log.info("Test teacher created: {}", teacher);
            } else {
                log.info("Test teacher already exists, skipping creation.");
                teacher = teacherRepository.findByEmail("teacher@test.com").orElseThrow();
            }

            Task task = new Task();
            if (taskRepository.findByTeacherId(teacher.getId()).isEmpty()) {
                task.setTeacherId(teacher.getId());
                task.setPriority(5);
                task.setFileIds(Set.of());
                task.setAssignedStudentId(student.getId());
                task.setDoneDate(LocalDateTime.now());
                task.setCreationDate(LocalDateTime.now());
                task.setName("Test task");
                task.setDescription("This is a test task description.");
                task = taskRepository.save(task);
                log.info("Test task created: {}", task);
            } else {
                log.info("Test task already exists, skipping creation.");
                task = taskRepository.findByTeacherId(teacher.getId()).get(0);
            }

            Project project = new Project();
            if (projectRepository.findByTeacherId(teacher.getId()).isEmpty()) {
                project.setTeacherId(teacher.getId());
                project.setStudentIds(Set.of(student.getId()));
                project.setName("Test Project");
                project.setDescription("This is a test project description.");
                project.setCreationDate(LocalDateTime.now());
                project.setTaskIds(Set.of(task.getId()));
                project = projectRepository.save(project);
                log.info("Test project created: {}", project);
            } else {
                log.info("Test project already exists, skipping creation.");
            }
        } catch (Exception e) {
            log.error("Error creating test resources: " + e.toString());
        }
    }
}
