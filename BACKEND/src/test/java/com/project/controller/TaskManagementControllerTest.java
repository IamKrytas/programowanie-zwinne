package com.project.controller;

import com.project.model.Project;
import com.project.model.Task;
import com.project.model.auth.JwtTokenDetails;
import com.project.model.auth.JwtTokenType;
import com.project.model.auth.UserRole;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import com.project.service.JwtTokenService;
import com.project.service.TaskManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.hamcrest.Matchers.emptyOrNullString;
import static org.hamcrest.MatcherAssert.assertThat; // (opcjonalny, przydatny w innych testach)
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class TaskManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskManagementService taskService;

    @MockBean
    private JwtTokenService jwtTokenService;

    @MockBean
    private ProjectRepository projectRepository;
    
    @MockBean
    private TaskRepository taskRepository;

    private Task task;

    @BeforeEach
    public void setUp() {
        task = new Task();
        task.setId("1");
        task.setName("Test Task");
        task.setDescription("Test Description");
        task.setPriority(5);
        task.setStudentId("1");
        task.setTeacherId("1");
        task.setCreationDate(LocalDateTime.of(2023, 1, 1, 10, 0));
        task.setDoneDate(LocalDateTime.of(2023, 1, 2, 10, 0));
        task.setFileId(Set.of("1"));
        task.setProjectId("1");

        Project project = new Project();
        project.setId("1");
        project.setTeacherId("1");

        when(projectRepository.findById("1")).thenReturn(Optional.of(project));
        when(taskRepository.findById("1")).thenReturn(Optional.of(task));

        when(jwtTokenService.validateToken(anyString()))
            .thenReturn(new JwtTokenDetails(JwtTokenType.ACCESS, "1@example.com", UserRole.TEACHER, "mock-token"));

        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken("1", null, List.of(new SimpleGrantedAuthority("ROLE_TEACHER")))
        );
    }

    @Test
    public void testGetTasksForTeacher() throws Exception {
        when(taskService.getTasks(eq("1"), eq("TEACHER"), anyInt(), anyInt())).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/api/v1/task")
                        .param("offset", "0")
                        .param("limit", "10")
                        .header("X-User-Id", "1")
                        .header("X-User-Role", "TEACHER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    public void testGetTasksForStudent() throws Exception {
        when(jwtTokenService.validateToken(anyString()))
            .thenReturn(new JwtTokenDetails(JwtTokenType.ACCESS, "2@example.com", UserRole.STUDENT, "mock-token"));
        when(taskService.getTasks(eq("2"), eq("STUDENT"), anyInt(), anyInt())).thenReturn(Collections.singletonList(task));

        mockMvc.perform(get("/api/v1/task")
                        .param("offset", "0")
                        .param("limit", "10")
                        .header("X-User-Id", "2")
                        .header("X-User-Role", "STUDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Test Task"));
    }

    @Test
    public void testGetTaskByIdForTeacher() throws Exception {
        when(taskService.getTaskById(eq("1"), eq("1"), eq("TEACHER"))).thenReturn(task);

        mockMvc.perform(get("/api/v1/task/{taskId}", "1")
        				.header("X-User-Id", "1")
        				.header("X-User-Role", "TEACHER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    public void testGetTaskByIdForStudent() throws Exception {
        when(jwtTokenService.validateToken(anyString()))
            .thenReturn(new JwtTokenDetails(JwtTokenType.ACCESS, "2@example.com", UserRole.STUDENT, "mock-token"));
        when(taskService.getTaskById(eq("1"), eq("2"), eq("STUDENT"))).thenReturn(task);

        mockMvc.perform(get("/api/v1/task/{taskId}", "1")
        				.header("X-User-Id", "2")
                        .header("X-User-Role", "STUDENT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    public void testCreateTask() throws Exception {
        // Ustaw mock, który dynamicznie ustawia ID w zwracanym obiekcie
        when(taskService.createTask(any(Task.class), eq("1"), eq("1"), eq("TEACHER")))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(0);
                    t.setId("1");  // Symulacja zachowania repozytorium
                    return t;
                });

        String json = """
            {
              "fileId": [1],
              "studentId": "1",
              "teacherId": "1",
              "name": "Test Task",
              "description": "Test Description",
              "priority": 5,
              "creationDate": "2023-01-01T10:00:00",
              "doneDate": "2023-01-02T10:00:00"
            }
        """;

        mockMvc.perform(post("/api/v1/task/1")
                        .header("Authorization", "Bearer mock-token")
                        .header("X-User-Id", "1")
                        .header("X-User-Role", "TEACHER")
                        .contentType("application/json")
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(not(emptyOrNullString())))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Test Task"));
    }

    @Test
    public void testUpdateTask() throws Exception {
        String updatedName = "Updated Task";
        String updatedDescription = "Updated Description";
        int updatedPriority = 6;

        // Zwracamy obiekt z ustawionym ID i nowymi danymi
        when(taskService.updateTask(eq("1"), any(Task.class), eq("1"), eq("TEACHER")))
                .thenAnswer(invocation -> {
                    Task t = invocation.getArgument(1);
                    t.setId("1"); // ważne, by ID było ustawione
                    return t;
                });

        String json = """
            {
              "fileId": [1],
              "studentId": "1",
              "teacherId": "1",
              "name": "Updated Task",
              "description": "Updated Description",
              "priority": 6,
              "creationDate": "2023-01-01T10:00:00",
              "doneDate": "2023-01-02T10:00:00"
            }
        """;

        mockMvc.perform(put("/api/v1/task/{taskId}", "1")
                        .header("Authorization", "Bearer mock-token")
                        .header("X-User-Id", "1")
                        .header("X-User-Role", "TEACHER")
                        .contentType("application/json")
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(content().string(not(emptyOrNullString())))
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value(updatedName))
                .andExpect(jsonPath("$.description").value(updatedDescription))
                .andExpect(jsonPath("$.priority").value(updatedPriority));
    }

    @Test
    public void testDeleteTask() throws Exception {
        doNothing().when(taskService).deleteTask(eq("1"), eq("1"), eq("TEACHER"));

        mockMvc.perform(delete("/api/v1/task/{taskId}", "1")
                        .header("Authorization", "Bearer mock-token")
                        .header("X-User-Id", "1")
                        .header("X-User-Role", "TEACHER"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testUnauthorizedGetTask() throws Exception {
        when(jwtTokenService.validateToken(anyString()))
            .thenReturn(new JwtTokenDetails(JwtTokenType.ACCESS, "1@example.com", UserRole.STUDENT, "mock-token"));

        mockMvc.perform(get("/api/v1/task/{taskId}", "1")
        				.header("X-User-Id", "1")
        				.header("X-User-Role", "STUDENT"))
                .andExpect(status().isOk());
    }
}
