package com.project;

import com.project.config.StartupConfig;
import com.project.model.Project;
import com.project.model.Task;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StudentIntegrationTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private StartupConfig startupConfig;
    @Autowired private ProjectRepository projectRepository;
    @Autowired private TaskRepository taskRepository;
    private HttpHeaders authHeaders;

    @BeforeEach
    public void setUp() {
        startupConfig.createTestResources();
        JwtTokenPair adminTokens = IntegrationTestUtils.login(restTemplate, port, new LoginCredentials("student@test.com", "test1234"));
        authHeaders = IntegrationTestUtils.getAuthHeaders(adminTokens);
    }

    @Test
    void test_loginAndRetrieveProjects() {
        HttpEntity<Void> request = new HttpEntity<>(authHeaders);
        String url = "http://localhost:%d/api/v1/project".formatted(port);

        ResponseEntity<List<Project>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).isNotEmpty();  // upewnij się, że projekt istnieje
    }

    @Test
    void testGetTask_forbidden() {
        Task task = taskRepository.findAll().get(0);
        HttpEntity<Void> request = new HttpEntity<>(authHeaders);

        // student nie jest przypisany do zadania => 403
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/task/" + task.getId(),
                HttpMethod.GET,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void test_getNonExistingTask_withTeacher() {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
        String nonExistingTaskId = UUID.randomUUID().toString();

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/task/" + nonExistingTaskId,
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testGetTasks_forbidden() {
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders);
        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/task",
                HttpMethod.GET,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void test_studentCannotCreateTask() {
        String projectId = projectRepository.findAll().get(0).getId();

        Task task = new Task("Forbidden Task", "Test description", "OPEN", new HashSet<>(),
                "creatorId", "assigneeId", projectId, 1,
                LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        HttpEntity<Task> request = new HttpEntity<>(task, authHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/task/" + projectId,
                HttpMethod.POST,
                request,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void testStudentCannotDeleteTask() {
        String taskId = taskRepository.findAll().get(0).getId();
        HttpEntity<Void> entity = new HttpEntity<>(authHeaders);

        ResponseEntity<String> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/task/" + taskId,
                HttpMethod.DELETE,
                entity,
                String.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}
