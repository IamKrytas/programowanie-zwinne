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

    private Task sampleTask;
    private Project sampleProject;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        sampleTask = new Task("task1", Set.of("1"), "2", "Task Name", "Description", 5,
        	    LocalDateTime.now(), LocalDateTime.now(), "NEW", "1");
        sampleProject = new Project();
        sampleProject.setId("proj1");
        sampleProject.setTeacherId("1");
        sampleProject.setStudents(Set.of("2"));
        sampleProject.setTasks(Set.of(sampleTask));
    }

    @Test
    void testGetTasksForTeacher() {
        when(projectRepository.findByTeacherId(eq("1"), any())).thenReturn(List.of(sampleProject));
        List<Task> tasks = taskService.getTasks("1", "TEACHER", 0, 10);
        assertEquals(1, tasks.size());
        assertEquals("task1", tasks.get(0).getId());
    }

    @Test
    void testGetTasksForStudent() {
        when(projectRepository.findByStudentsContaining(eq("2"), any())).thenReturn(List.of(sampleProject));
        List<Task> tasks = taskService.getTasks("2", "STUDENT", 0, 10);
        assertEquals(1, tasks.size());
        assertEquals("2", tasks.get(0).getStudentId());
    }

    @Test
    void testGetTaskByIdAuthorizedTeacher() {
        when(projectRepository.findAll()).thenReturn(List.of(sampleProject));
        Task result = taskService.getTaskById("task1", "1", "TEACHER");
        assertNotNull(result);
    }

    @Test
    void testGetTaskByIdAuthorizedStudent() {
        when(projectRepository.findAll()).thenReturn(List.of(sampleProject));
        Task result = taskService.getTaskById("task1", "2", "STUDENT");
        assertNotNull(result);
    }

    @Test
    void testCreateTask() {
        when(projectRepository.findById("proj1")).thenReturn(Optional.of(sampleProject));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask, "proj1", "1", "TEACHER");
        assertEquals("task1", result.getId());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        sampleTask.setName("Updated Name");
        Task result = taskService.updateTask("task1", sampleTask, "1", "TEACHER");
        assertEquals("Updated Name", result.getName());
    }

    @Test
    void testDeleteTask() {
        when(taskRepository.findById("task1")).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).deleteById("task1");

        assertDoesNotThrow(() -> taskService.deleteTask("task1", "1", "TEACHER"));
    }
} 
