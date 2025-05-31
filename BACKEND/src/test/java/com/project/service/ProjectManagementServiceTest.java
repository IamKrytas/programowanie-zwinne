package com.project.service;

import com.project.model.Project;
import com.project.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProjectManagementServiceTest {

    @Mock
    private ProjectRepository projectRepository;


    @InjectMocks
    private ProjectManagementService projectService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetProjectsForTeacher() {
        Project project = new Project();
        project.setId("p1");
        project.setTeacherId("1");

        when(projectRepository.findByTeacherId(eq("1"), any())).thenReturn(List.of(project));

        List<Project> result = projectService.getProjects("1", "TEACHER", 0, 10);
        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).getId());
    }

    @Test
    public void testGetProjectByIdForStudentAuthorized() {
        Project project = new Project();
        project.setId("p1");
        project.setStudentIds(Set.of("2"));

        when(projectRepository.findByIdAndStudentIdsContaining("p1", "2")).thenReturn(Optional.of(project));

        Project result = projectService.getProjectById("p1", "2", "STUDENT");
        assertEquals("p1", result.getId());
    }

    @Test
    public void testCreateProject() {
        Project project = new Project();
        project.setName("New Project");
        project.setStudentIds(Set.of("1"));

        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.createProject("100", "TEACHER", project);
        assertEquals("New Project", result.getName());
        assertEquals("100", result.getTeacherId());
    }

    @Test
    public void testUpdateProject() {
        Project existing = new Project();
        existing.setId("p1");
        existing.setTeacherId("100");

        Project update = new Project();
        update.setName("Updated");
        update.setStudentIds(Set.of());

        when(projectRepository.findById("p1")).thenReturn(Optional.of(existing));
        when(projectRepository.save(any(Project.class))).thenAnswer(i -> i.getArgument(0));

        Project result = projectService.updateProject("p1", "100", "TEACHER", update);
        assertEquals("Updated", result.getName());
    }

    @Test
    public void testDeleteProject() {
        Project project = new Project();
        project.setId("p1");
        project.setTeacherId("100");

        when(projectRepository.findById("p1")).thenReturn(Optional.of(project));

        projectService.deleteProject("p1", "100", "TEACHER");

        verify(projectRepository, times(1)).deleteById("p1");
    }
}
