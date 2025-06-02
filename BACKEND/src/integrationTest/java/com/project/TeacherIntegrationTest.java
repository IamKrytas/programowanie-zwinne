package com.project;

import com.project.config.StartupConfig;
import com.project.model.Project;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherIntegrationTest {
    private @LocalServerPort int port;
    private @Autowired TestRestTemplate restTemplate;
    private @Autowired StartupConfig startupConfig;

    private final LoginCredentials teacherCredentials = new LoginCredentials("teacher@test.com", "teacher1234");

    @BeforeEach
    public void setUp() {
        startupConfig.createTestResources();
    }

    @Test
    public void test_shouldLoginAsTeacherSuccessfully() {
        String url = "http://localhost:%d/api/v1/auth/login".formatted(port);
        ResponseEntity<JwtTokenPair> response = restTemplate.postForEntity(url, teacherCredentials, JwtTokenPair.class);
        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody().getAccessToken()).isNotBlank();
        assertThat(response.getBody().getRefreshToken()).isNotBlank();
    }

    @Test
    public void test_loginAndRetrieveProjects() {
        String url = "http://localhost:%d/api/v1/auth/login".formatted(port);
        JwtTokenPair jwtTokenPair = restTemplate.postForObject(url, teacherCredentials, JwtTokenPair.class);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtTokenPair.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        String projectsUrl = "http://localhost:%d/api/v1/project".formatted(port);
        ResponseEntity<List<Project>> projectsResponse = restTemplate.exchange(
            projectsUrl,
            HttpMethod.GET,
            requestEntity,
            new ParameterizedTypeReference<>() {}
        );

        assertThat(projectsResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(projectsResponse.getBody()).isNotEmpty();
    }
}
