package com.project;

import com.project.config.StartupConfig;
import com.project.model.Project;
import com.project.model.Task;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import com.project.repository.ProjectRepository;
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

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TeacherIntegrationTest {

    private @LocalServerPort int port;
    private @Autowired TestRestTemplate restTemplate;
    private @Autowired StartupConfig startupConfig;
    private @Autowired ProjectRepository projectRepository;
    private HttpHeaders authHeaders;

    @BeforeEach
    public void setUp() {
        startupConfig.createTestResources();
        JwtTokenPair adminTokens = IntegrationTestUtils.login(restTemplate, port, new LoginCredentials("teacher@test.com", "test1234"));
        authHeaders = IntegrationTestUtils.getAuthHeaders(adminTokens);
    }

    @Test
    public void test_loginAndRetrieveProjects() {
        HttpEntity<Void> request = new HttpEntity<>(authHeaders);
        String url = "http://localhost:%d/api/v1/project".formatted(port);
        ResponseEntity<List<Project>> projectsResponse = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {}
        );

        assertThat(projectsResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(projectsResponse.getBody()).isNotNull();
        assertThat(projectsResponse.getBody()).isNotEmpty();
    }

    @Test
    void testCreateProject() {
        Project project = new Project(
        	    null, // id
        	    Set.of("fileX"), // fileIds
        	    null, // fileZipId
        	    Set.of("taskX"), // taskIds
        	    Set.of("existingStudentId"), // studentIds
        	    "Updated Teacher Project", // name
        	    "Description", // description
        	    LocalDateTime.now().plusDays(7), // deadline
        	    LocalDateTime.now() // creationDate
        	);
        
        project.setTeacherId("existingTeacherId");

        HttpEntity<Project> request = new HttpEntity<>(project, authHeaders);

        ResponseEntity<Project> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/project",
                HttpMethod.POST,
                request,
                Project.class
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    void testUpdateProject() {
        String id = projectRepository.findAll().get(0).getId();

        Project updated = new Project(
                id,
                Set.of("file2"),
                null,
                Set.of("task2"),
                Set.of("existingStudentId"),
                "Updated Teacher Project",
                "Updated description for the project",
                LocalDateTime.now().plusDays(7),
                null
        );

        HttpEntity<Project> updateRequest = new HttpEntity<>(updated, authHeaders);

        restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/project/" + id,
                HttpMethod.PUT,
                updateRequest,
                Void.class
        );

        HttpEntity<Void> getRequest = new HttpEntity<>(authHeaders);
        ResponseEntity<Project> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/project/" + id,
                HttpMethod.GET,
                getRequest,
                Project.class
        );

        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Updated Teacher Project");
    }
    
    private Project createProjectWithTeacher(JwtTokenPair jwt) {
        Project project = new Project(
                null,
                Set.of("file1"),
                null,
                Set.of("task1"),
                Set.of("existingStudentId"),
                "Initial Teacher Project",
                "Initial description",
                LocalDateTime.now().plusDays(3),
                null
        );
        project.setTeacherId("existingTeacherId");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Project> request = new HttpEntity<>(project, headers);

        ResponseEntity<Project> response = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/project",
                HttpMethod.POST,
                request,
                Project.class
        );

        return response.getBody();
    }
}
