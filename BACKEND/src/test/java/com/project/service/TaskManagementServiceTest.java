package com.project.service;

import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

        sampleProject = new Project();
        sampleProject.setId(1L);
        sampleProject.setFileIds(Set.of());
        sampleProject.setTeacherId("teach1");
        sampleProject.setTaskIds(Set.of("1"));
        sampleProject.setStudentIds(Set.of("stu1"));
        sampleProject.setName("name");
        sampleProject.setDescription("desc");
        sampleProject.setCreationDate(LocalDateTime.now());
        sampleProject.setDoneDate(LocalDateTime.now());
        
        sampleTask = new Task();
        sampleTask.setId(1L);
        sampleTask.setProjectId("1");
        sampleTask.setTeacherId("teach1");
        sampleTask.setFileIds(Set.of(""));
        sampleTask.setAssignedStudentId("2");
        sampleTask.setName("task1");
        sampleTask.setDescription("Description");
        sampleTask.setPriority(1);
        sampleTask.setCreationDate(LocalDateTime.now());
        sampleTask.setDoneDate(LocalDateTime.now());
    }

    @Test
    void testGetTasksForTeacher() {
        when(taskRepository.findByTeacherId(any(), any())).thenReturn(List.of(sampleTask));
        List<Task> tasks = taskService.getTasks("teach1", "TEACHER", 0, 10);

        assertEquals(1, tasks.size());
        assertEquals(1L, tasks.get(0).getId());
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
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        Task result = taskService.getTaskById("1", "teach1", "TEACHER");
        assertNotNull(result);
    }

    @Test
    void testGetTaskByIdAuthorizedStudent() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        Task result = taskService.getTaskById("1", "2", "STUDENT");
        assertNotNull(result);
    }

    @Test
    @Disabled
    void testCreateTask() {
        when(projectRepository.findById(1L)).thenReturn(Optional.of(sampleProject));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        Task result = taskService.createTask(sampleTask, "1", "teach1", "TEACHER");
        assertEquals("1", result.getProjectId());
    }

    @Test
    void testUpdateTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        when(taskRepository.save(any(Task.class))).thenReturn(sampleTask);

        sampleTask.setName("Updated Name");
        Task result = taskService.updateTask("1", sampleTask, "teach1", "TEACHER");
        assertEquals("Updated Name", result.getName());
    }

    @Test
    @Disabled
    void testDeleteTask() {
        when(taskRepository.findById(1L)).thenReturn(Optional.of(sampleTask));
        doNothing().when(taskRepository).deleteById(1L);
        assertDoesNotThrow(() -> taskService.deleteTask("1", "teach1", "TEACHER"));
    }
} 
