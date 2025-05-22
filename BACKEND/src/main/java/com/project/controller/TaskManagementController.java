package com.project.controller;

import com.project.model.Task;
import com.project.service.TaskManagementService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class TaskManagementController {

    private final TaskManagementService taskService;

    @GetMapping("/task")
    public ResponseEntity<List<Task>> getTasks(
    		@RequestAttribute("id") String userId,
    		@RequestAttribute("role") String role,
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {

        List<Task> tasks = taskService.getTasks(userId, role, offset, limit);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/task/{taskId}")
    public ResponseEntity<Task> getTaskById(
            @PathVariable String taskId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        Task task = taskService.getTaskById(taskId, userId, role);
        return ResponseEntity.ok(task);
    }

    @PostMapping("/task/{projectId}")
    public ResponseEntity<Task> createTask(
            @RequestBody Task task,
            @PathVariable String projectId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        Task createdTask = taskService.createTask(task, projectId, userId, role);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(createdTask);
    }

    @PutMapping("/task/{taskId}")
    public ResponseEntity<Task> updateTask(
            @PathVariable String taskId,
            @RequestBody Task updatedTask,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        Task task = taskService.updateTask(taskId, updatedTask, userId, role);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(task);
    }

    @DeleteMapping("/task/{taskId}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String taskId,
            @RequestAttribute("id") String userId,
            @RequestAttribute("role") String role) {

        taskService.deleteTask(taskId, userId, role);
        return ResponseEntity.noContent().build();
    }
}


