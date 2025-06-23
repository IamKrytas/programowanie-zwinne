package com.project.service;

import com.project.model.AppStats;
import com.project.model.Teacher;
import com.project.repository.ProjectRepository;
import com.project.repository.StudentRepository;
import com.project.repository.TaskRepository;
import com.project.repository.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public AppStats getAppStats() {
        long totalTeachers = teacherRepository.count();
        long totalStudents = studentRepository.count();
        long totalProjects = projectRepository.count();
        long totalTasks = taskRepository.count();
        return new AppStats(totalTeachers, totalStudents, totalProjects, totalTasks);
    }
}
