package com.project.service;

import com.project.model.Task;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskManagementService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public List<Task> getTasks(String userId, String role, int offset, int limit) {
        log.info("Fetching tasks for user: {}, role: {}, offset: {}, limit: {}", userId, role, offset, limit);
        Pageable pageable = PageRequest.of(offset / limit, limit);

        if ("TEACHER".equalsIgnoreCase(role)) {
            return taskRepository.findByTeacherId(userId, pageable).stream().toList();
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return taskRepository.findByAssignedStudentId(userId, pageable).stream().toList();
        } else {
            log.warn("Unauthorized role '{}' tried to fetch tasks", role);
            throw new SecurityException("Unauthorized role");
        }
    }

    public Task getTaskById(String taskId, String userId, String role) {
        log.info("Fetching task with ID: {} by user: {} with role: {}", taskId, userId, role);
        Task task = taskRepository.findById(taskId).orElseThrow();

        if ("TEACHER".equalsIgnoreCase(role) && String.valueOf(task.getTeacherId()).equals(userId)) {
            return task;
        }

        if ("STUDENT".equalsIgnoreCase(role) && String.valueOf(task.getAssignedStudentId()).equals(userId)) {
            return task;
        }

        log.warn("User: {} attempted unauthorized access to task: {}", userId, taskId);
        throw new SecurityException("Unauthorized access to task");
    }

    public Task createTask(Task task, String projectId, String userId, String role) {
        log.info("User: {} attempting to create task in project: {}, role: {}", userId, projectId, role);
        if ("TEACHER".equalsIgnoreCase(role)) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> {
                        log.warn("Project with ID: {} not found", projectId);
                        return new RuntimeException("Project not found");
                    });

            if (!String.valueOf(project.getTeacherId()).equals(userId)) {
                log.warn("User: {} not authorized to create task in project: {}", userId, projectId);
                throw new SecurityException("Unauthorized to create task for this project");
            }

            Task saved = taskRepository.save(task);
            log.info("Task created successfully with ID: {} in project: {}", saved.getId(), projectId);
            return saved;
        } else {
            log.warn("Unauthorized role '{}' tried to create task", role);
            throw new SecurityException("Unauthorized role");
        }
    }

    public Task updateTask(String taskId, Task updatedTask, String userId, String role) {
        log.info("Updating task with ID: {} by user: {} with role: {}", taskId, userId, role);
        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task with ID: {} not found", taskId);
                    return new RuntimeException("Task not found");
                });

        if ("TEACHER".equalsIgnoreCase(role) && existingTask.getTeacherId().equals(userId)) {
            existingTask.setName(updatedTask.getName());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDoneDate(updatedTask.getDoneDate());
            existingTask.setCreationDate(updatedTask.getCreationDate());

            Task saved = taskRepository.save(existingTask);
            log.info("Task with ID: {} updated successfully by user: {}", saved.getId(), userId);
            return saved;
        } else {
            log.warn("User: {} unauthorized to update task: {}", userId, taskId);
            throw new SecurityException("Unauthorized access to update task");
        }
    }

    public void deleteTask(String taskId, String userId, String role) {
        log.info("Attempting to delete task with ID: {} by user: {} with role: {}", taskId, userId, role);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.warn("Task with ID: {} not found", taskId);
                    return new RuntimeException("Task not found");
                });

        if ("TEACHER".equalsIgnoreCase(role) && task.getTeacherId().equals(userId)) {
            taskRepository.delete(task);
            log.info("Task with ID: {} deleted successfully by user: {}", taskId, userId);
        } else {
            log.warn("User: {} unauthorized to delete task: {}", userId, taskId);
            throw new SecurityException("Unauthorized access to delete task");
        }
    }
}
