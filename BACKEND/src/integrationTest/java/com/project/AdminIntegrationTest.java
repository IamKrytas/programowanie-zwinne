package com.project;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import com.project.config.StartupConfig;
import com.project.model.*;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import com.project.model.auth.UserRole;
import com.project.repository.TaskRepository;
import com.project.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AdminIntegrationTest {

    @LocalServerPort private int port;
    @Autowired private TestRestTemplate restTemplate;
    @Autowired private StartupConfig startupConfig;
    @Autowired private TaskRepository taskRepository;
    @Autowired private TeacherRepository teacherRepository;
    private HttpHeaders authHeaders;

    @BeforeEach
    public void setUp() {
        startupConfig.createTestResources();
        JwtTokenPair adminTokens = IntegrationTestUtils.login(restTemplate, port, new LoginCredentials("admin@example.com", "admin"));
        authHeaders = IntegrationTestUtils.getAuthHeaders(adminTokens);
    }

    @Test
    public void test_loginAndRetrieveStudents() {
        HttpEntity<Void> request = new HttpEntity<>(authHeaders);
        String url = "http://localhost:%d/api/v1/admin/student".formatted(port);
        ResponseEntity<List<Student>> response = restTemplate.exchange(
                url, HttpMethod.GET, request, new ParameterizedTypeReference<>() {}
        );

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void testCreateUser() {
        String uniqueEmail = "jan.kowalski+" + UUID.randomUUID() + "@example.com";
        Teacher newTeacher = new Teacher(null, "Jan", "Kowalski", uniqueEmail, "password123");

        HttpEntity<Teacher> requestEntity = new HttpEntity<>(newTeacher, authHeaders);
        ResponseEntity<Teacher> response = restTemplate.exchange(
                "http://localhost:%d/api/v1/admin/teacher".formatted(port),
                HttpMethod.POST, requestEntity, Teacher.class
        );

        System.out.println("CreateUser response: " + response.getStatusCode());
        if (response.getBody() != null) {
            System.out.println("Created user ID: " + response.getBody().getId());
        }

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getEmail()).isEqualTo(uniqueEmail);
    }

    @Test
    public void testUpdateUser() {
        String teacherId = teacherRepository.findAll().get(0).getId();
        Teacher updatedTeacher = new Teacher(teacherId, "Updated", "User", "updated@example.com", "newpassword123");

        HttpEntity<Teacher> requestEntity = new HttpEntity<>(updatedTeacher, authHeaders);

        restTemplate.exchange(
                "http://localhost:%d/api/v1/admin/teacher/%s".formatted(port, teacherId),
                HttpMethod.PUT, requestEntity, Void.class
        );

        ResponseEntity<Teacher> response = restTemplate.exchange(
                "http://localhost:%d/api/v1/admin/teacher/%s".formatted(port, teacherId),
                HttpMethod.GET, new HttpEntity<>(authHeaders), Teacher.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated");
    }

    @Test
    public void testDeleteUser() {
        String teacherId = taskRepository.findAll().get(0).getId();
        HttpEntity<Void> request = new HttpEntity<>(authHeaders);
        restTemplate.exchange(
                "http://localhost:%d/api/v1/admin/teacher/%s".formatted(port, teacherId),
                HttpMethod.DELETE, request, Void.class
        );

        ResponseEntity<Teacher> response = restTemplate.exchange(
                "http://localhost:%d/api/v1/admin/teacher/%s".formatted(port, teacherId),
                HttpMethod.GET, request, Teacher.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
