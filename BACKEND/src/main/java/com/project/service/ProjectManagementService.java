package com.project.service;

import com.project.model.Project;
import com.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectManagementService {

    private final ProjectRepository projectRepository;

    public List<Project> getProjects(String userId, String role, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);

        if ("TEACHER".equalsIgnoreCase(role)) {
            return projectRepository.findByTeacherId(userId, pageable);
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return projectRepository.findByStudentsContaining(userId, pageable);
        } else {
            throw new SecurityException("Unauthorized role");
        }
    }

    public Project getProjectById(String projectId, String userId, String role) {
        if ("TEACHER".equalsIgnoreCase(role)) {
            return projectRepository.findByIdAndTeacherId(projectId, userId)
                    .orElseThrow(() -> new RuntimeException("Project not found or unauthorized"));
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return projectRepository.findByIdAndStudentsContaining(projectId, userId)
                    .orElseThrow(() -> new RuntimeException("Project not found or unauthorized"));
        } else {
            throw new SecurityException("Unauthorized role");
        }
    }

    public Project createProject(String userId, String role, Project project) {
        if (!"TEACHER".equalsIgnoreCase(role)) {
            throw new SecurityException("Only teachers can create projects.");
        }

        project.setTeacherId(userId);
        project.setCreationDate(LocalDateTime.now());

        return projectRepository.save(project);
    }

    public Project updateProject(String projectId, String userId, String role, Project updatedProject) {
        if (!"TEACHER".equalsIgnoreCase(role)) {
            throw new SecurityException("Only teachers can update projects.");
        }

        Project existing = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!existing.getTeacherId().equals(userId)) {
            throw new SecurityException("You can only update your own projects.");
        }

        updatedProject.setId(projectId);
        updatedProject.setTeacherId(userId);
        updatedProject.setCreationDate(existing.getCreationDate());

        return projectRepository.save(updatedProject);
    }

    public void deleteProject(String projectId, String userId, String role) {
        if (!"TEACHER".equalsIgnoreCase(role)) {
            throw new SecurityException("Only teachers can delete projects.");
        }

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        if (!project.getTeacherId().equals(userId)) {
            throw new SecurityException("You can only delete your own projects.");
        }

        projectRepository.deleteById(projectId);
    }
}

