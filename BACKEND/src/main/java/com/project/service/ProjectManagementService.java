package com.project.service;

import com.project.model.Project;
import com.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectManagementService {

    private final ProjectRepository projectRepository;

    public List<Project> getProjects(String userId, String role, int offset, int limit) {
        log.info("Fetching projects for user: {}, role: {}, offset: {}, limit: {}", userId, role, offset, limit);
        Pageable pageable = PageRequest.of(offset / limit, limit);

        if ("TEACHER".equalsIgnoreCase(role)) {
            return projectRepository.findByTeacherId(userId, pageable);
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return projectRepository.findByStudentIdsContaining(userId, pageable);
        } else {
            log.warn("Unauthorized role '{}' tried to fetch projects", role);
            throw new SecurityException("Unauthorized role");
        }
    }

    public Project getProjectById(String projectId, String userId, String role) {
        log.info("Fetching project with ID: {} for user: {} with role: {}", projectId, userId, role);
        if ("TEACHER".equalsIgnoreCase(role)) {
            return projectRepository.findByIdAndTeacherId(projectId, userId)
                    .orElseThrow(() -> new RuntimeException("Project not found or unauthorized"));
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return projectRepository.findByIdAndStudentIdsContaining(projectId, userId)
                    .orElseThrow(() -> new RuntimeException("Project not found or unauthorized"));
        } else {
            log.warn("Unauthorized role '{}' tried to access project", role);
            throw new SecurityException("Unauthorized role");
        }
    }

    public Project createProject(String userId, String role, Project project) {
        log.info("Attempting to create project by user: {} with role: {}", userId, role);
        if (!"TEACHER".equalsIgnoreCase(role)) {
            log.warn("Unauthorized project creation attempt by user: {}", userId);
            throw new SecurityException("Only teachers can create projects.");
        }

        project.setTeacherId(userId);
        project.setCreationDate(LocalDateTime.now());

        Project saved = projectRepository.save(project);
        log.info("Project created successfully with ID: {} by teacher: {}", saved.getId(), userId);
        return saved;
    }

    public Project updateProject(String projectId, String userId, String role, Project updatedProject) {
        log.info("Updating project with ID: {} by user: {} with role: {}", projectId, userId, role);
        if (!"TEACHER".equalsIgnoreCase(role)) {
            log.warn("Unauthorized project update attempt by user: {}", userId);
            throw new SecurityException("Only teachers can update projects.");
        }

        Project existing = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!existing.getTeacherId().equals(userId)) {
            log.warn("User {} tried to update project {} they do not own", userId, projectId);
            throw new SecurityException("You can only update your own projects.");
        }

        updatedProject.setId(projectId);
        updatedProject.setTeacherId(userId);
        updatedProject.setCreationDate(existing.getCreationDate());

        Project saved = projectRepository.save(updatedProject);
        log.info("Project with ID: {} updated successfully by user: {}", saved.getId(), userId);
        return saved;
    }

    public void deleteProject(String projectId, String userId, String role) {
        log.info("Attempting to delete project with ID: {} by user: {} with role: {}", projectId, userId, role);
        if (!"TEACHER".equalsIgnoreCase(role)) {
            log.warn("Unauthorized project deletion attempt by user: {}", userId);
            throw new SecurityException("Only teachers can delete projects.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getTeacherId().equals(userId)) {
            log.warn("User {} tried to delete project {} they do not own", userId, projectId);
            throw new SecurityException("You can only delete your own projects.");
        }

        projectRepository.deleteById(projectId);
        log.info("Project with ID: {} deleted successfully by user: {}", projectId, userId);
    }
}
