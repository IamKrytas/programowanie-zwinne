package com.project.controller;

import com.project.model.Project;
import com.project.model.Task;
import com.project.service.ProjectFileService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ProjectFileController {
    private final ProjectFileService projectFileService;

    @GetMapping("/project/{projectId}/file/{fileId}")
    @SneakyThrows({IOException.class})
    public ResponseEntity<InputStreamResource> getProjectFile(@PathVariable String projectId, @PathVariable String fileId) {
        InputStream stream = projectFileService.getProjectFile(projectId, fileId);
        InputStreamResource resource = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .body(resource);
    }

    @PostMapping("/project/{projectId}/file")
    @SneakyThrows
    public ResponseEntity<Project> uploadProjectFile(@RequestParam("file") MultipartFile file, @PathVariable String projectId) {
        String filename = file.getOriginalFilename();
        Project project = projectFileService.uploadProjectFile(file.getInputStream(), projectId, filename);

        return ResponseEntity.ok(project);
    }

    @DeleteMapping("/project/{projectId}/file/{fileId}")
    @SneakyThrows
    public ResponseEntity<Project> deleteProjectFile(@PathVariable String projectId, @PathVariable String fileId) {
        Project project = projectFileService.deleteProjectFile(projectId, fileId);

        return ResponseEntity.ok(project);
    }

    @GetMapping("/project/{taskId}/file/{fileId}")
    @SneakyThrows({IOException.class})
    public ResponseEntity<InputStreamResource> getTaskFile(@PathVariable String taskId, @PathVariable String fileId) {
        InputStream stream = projectFileService.getTaskFile(taskId, fileId);
        InputStreamResource resource = new InputStreamResource(stream);

        return ResponseEntity.ok()
                .body(resource);
    }

    @PostMapping("/project/{taskId}/file")
    @SneakyThrows
    public ResponseEntity<Task> uploadTaskFile(@RequestParam("file") MultipartFile file, @PathVariable String taskId) {
        String filename = file.getOriginalFilename();
        Task task = projectFileService.uploadTaskFile(file.getInputStream(), taskId, filename);

        return ResponseEntity.ok(task);
    }

    @DeleteMapping("/project/{taskId}/file/{fileId}")
    @SneakyThrows
    public ResponseEntity<Task> deleteTaskFile(@PathVariable String taskId, @PathVariable String fileId) {
        Task task = projectFileService.deleteTaskFile(taskId, fileId);

        return ResponseEntity.ok(task);
    }
}