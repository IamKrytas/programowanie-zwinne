package com.project.service;

import com.project.model.Task;
import com.project.model.Project;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskManagementService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    public List<Task> getTasks(String userId, String role, int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);

        if ("TEACHER".equalsIgnoreCase(role)) {
            return projectRepository.findByTeacherId(userId, pageable).stream()
                    .flatMap(p -> p.getTasks().stream())
                    .skip(offset).limit(limit)
                    .collect(Collectors.toList());
        } else if ("STUDENT".equalsIgnoreCase(role)) {
            return projectRepository.findByStudentsContaining(userId, pageable).stream()
                    .flatMap(p -> p.getTasks().stream().filter(t -> String.valueOf(t.getStudentId()).equals(userId)))
                    .skip(offset).limit(limit)
                    .collect(Collectors.toList());
        } else {
            throw new SecurityException("Unauthorized role");
        }
    }

    public Task getTaskById(String taskId, String userId, String role) {
        int userIntId = Integer.parseInt(userId);

        Optional<Project> project = projectRepository.findAll().stream()
                .filter(p -> p.getTasks().stream().anyMatch(t -> t.getId().equals(taskId)))
                .findFirst();

        if (project.isEmpty()) {
            throw new RuntimeException("Task not found");
        }

        Task task = project.get().getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if ("TEACHER".equalsIgnoreCase(role) && String.valueOf(project.get().getTeacherId()).equals(userId)) {  // Konwersja na String
            return task;
        }

        if ("STUDENT".equalsIgnoreCase(role) && String.valueOf(task.getStudentId()).equals(userId)) {  // Konwersja na String
            return task;
        }

        throw new SecurityException("Unauthorized access to task");
    }
    
    public Task createTask(Task task, String projectId, String userId, String role) {
        if ("TEACHER".equalsIgnoreCase(role)) {
            Project project = projectRepository.findById(projectId)
                    .orElseThrow(() -> new RuntimeException("Project not found"));

            if (!String.valueOf(project.getTeacherId()).equals(userId)) {  // Porównanie Stringów
                throw new SecurityException("Unauthorized to create task for this project");
            }

            task.setProjectId(projectId);
            task.setTeacherId(userId);  // Ustawiamy String, bo userId jest typu String

            return taskRepository.save(task);
        } else {
            throw new SecurityException("Unauthorized role");
        }
    }
    
    public Task updateTask(String taskId, Task updatedTask, String userId, String role) {
        int userIntId = Integer.parseInt(userId);

        Task existingTask = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if ("TEACHER".equalsIgnoreCase(role) && existingTask.getTeacherId().equals(String.valueOf(userIntId))) {
            // Aktualizujemy zadanie tylko jeśli nauczyciel jest jego twórcą
            existingTask.setName(updatedTask.getName());
            existingTask.setDescription(updatedTask.getDescription());
            existingTask.setPriority(updatedTask.getPriority());
            existingTask.setDoneDate(updatedTask.getDoneDate());
            existingTask.setCreationDate(updatedTask.getCreationDate());

            return taskRepository.save(existingTask); // Zapisujemy zaktualizowane zadanie
        } else {
            throw new SecurityException("Unauthorized access to update task");
        }
    }
    
    public void deleteTask(String taskId, String userId, String role) {
        int userIntId = Integer.parseInt(userId);

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        if ("TEACHER".equalsIgnoreCase(role) && task.getTeacherId().equals(String.valueOf(userIntId))) {
            taskRepository.delete(task); // Usuwamy zadanie
        } else {
            throw new SecurityException("Unauthorized access to delete task");
        }
    }
}
