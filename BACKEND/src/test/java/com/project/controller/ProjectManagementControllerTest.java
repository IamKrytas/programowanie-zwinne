package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Project;
import com.project.model.Student;
import com.project.service.ProjectManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProjectManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectManagementService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private Project sampleProject;

    @BeforeEach
    public void setup() {
        sampleProject = new Project();
        sampleProject.setId("p1");
        sampleProject.setName("Test Project");
        sampleProject.setStudents(Set.of("1"));
    }

    @Test
    public void testGetProjects() throws Exception {
        when(projectService.getProjects("1", "TEACHER", 0, 10)).thenReturn(List.of(sampleProject));

        mockMvc.perform(get("/api/v1/project")
                .param("offset", "0")
                .param("limit", "10")
                .requestAttr("id", "1")
                .requestAttr("role", "TEACHER"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetProjectById() throws Exception {
        when(projectService.getProjectById("p1", "1", "TEACHER")).thenReturn(sampleProject);

        mockMvc.perform(get("/api/v1/project/p1")
                .requestAttr("id", "1")
                .requestAttr("role", "TEACHER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("p1"));
    }

    @Test
    public void testCreateProject() throws Exception {
    	when(projectService.createProject(eq("1"), eq("TEACHER"), any(Project.class))).thenReturn(sampleProject);

        mockMvc.perform(post("/api/v1/project")
                .requestAttr("id", "1")
                .requestAttr("role", "TEACHER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleProject)))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateProject() throws Exception {
    	when(projectService.updateProject(eq("p1"), eq("1"), eq("TEACHER"), any(Project.class))).thenReturn(sampleProject);

        mockMvc.perform(put("/api/v1/project/p1")
                .requestAttr("id", "1")
                .requestAttr("role", "TEACHER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sampleProject)))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProject() throws Exception {
        doNothing().when(projectService).deleteProject("p1", "1", "TEACHER");

        mockMvc.perform(delete("/api/v1/project/p1")
                .requestAttr("id", "1")
                .requestAttr("role", "TEACHER"))
                .andExpect(status().isNoContent());
    }

}


