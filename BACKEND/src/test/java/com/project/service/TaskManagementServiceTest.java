package com.project.service;

import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TaskManagementServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private TaskManagementService taskService;

    private Project sampleProject;
    private Task sampleTask;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleProject = new Project("proj1", Set.of(), "teach1", Set.of("task1"), Set.of("stu1"), "name", "desc", LocalDateTime.now(), LocalDateTime.now());
        sampleTask = new Task("task1", "proj1", "teach1", Set.of(""), "2", "task1", "Description", 1, LocalDateTime.now(), LocalDateTime.now());
    }

    @Test
    void testGetTasksForTeacher() {
        when(taskRepository.findByTeacherId(any(), any())).thenReturn(List.of(sampleTask));
        List<Task> tasks = taskService.getTasks("teach1", "TEACHER", 0, 10);

        assertEquals(1, tasks.size());
        assertEquals("task1", tasks.get(0).getId());
    }

    @Test
    void testGetTasksForStudent() {
        when(taskRepository.findByAssignedStudentId(any(), any())).thenReturn(List.of(sampleTask));
        List<Task> tasks = taskService.getTasks("2", "STUDENT", 0, 10);
        assertEquals(1, tasks.size());
        assertEquals("2", tasks.get(0).getAssignedStudentId());
    }

    @Test
    void testGetTaskByIdAuthorizedTeacher() {
        when(taskRepository.findById(eq("task1"))).thenReturn(Optional.of(sampleTask));
        Task result = taskService.getTaskById("task1", "teach1", "TEACHER");
        assertNotNull(result);
    }

    @Test
    void testGetTaskByIdAuthorizedStudent() {
        when(taskRepository.findById(eq("task1"))).thenReturn(Optional.of(sampleTask));
        Task result = taskService.getTaskById("task1", "2", "STUDENT");
        assertNotNull(result);
    }

    @Test
    void testCreateTask() {
        when(projectRepository.findById(eq("proj1"))).thenReturn(Optional.of(sampleProject));
        when(taskRepository.findById(eq("task1"))).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask, "proj1", "teach1", "TEACHER");
        assertEquals("task1", result.getId());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        sampleTask.setName("Updated Name");
        Task result = taskService.updateTask("task1", sampleTask, "teach1", "TEACHER");
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).deleteById("task1");
        assertDoesNotThrow(() -> taskService.deleteTask("task1", "teach1", "TEACHER"));
    }
} 
