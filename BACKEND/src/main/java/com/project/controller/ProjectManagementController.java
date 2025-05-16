package com.project.controller;

import com.project.model.Project;
import com.project.service.ProjectManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectManagementController {

    private final ProjectManagementService projectService;

    @GetMapping("/project")
    public ResponseEntity<List<Project>> getProjects(
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        List<Project> projects = projectService.getProjects(userId, role, offset, limit);
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<Project> getProjectById(
            @PathVariable String projectId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        Project project = projectService.getProjectById(projectId, userId, role);
        return ResponseEntity.ok(project);
    }

    @PostMapping("/project")
    public ResponseEntity<Project> createProject(
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role,
            @RequestBody Project project) {

        Project created = projectService.createProject(userId, role, project);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/project/{projectId}")
    public ResponseEntity<Project> updateProject(
            @PathVariable String projectId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role,
            @RequestBody Project updatedProject) {

        Project project = projectService.updateProject(projectId, userId, role, updatedProject);
        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/project/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable String projectId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        projectService.deleteProject(projectId, userId, role);
        return ResponseEntity.noContent().build();
    }
}
