package com.project;

import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class IntegrationTestUtils {
    public static HttpHeaders getAuthHeaders(JwtTokenPair tokenPair) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenPair.getAccessToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    public static JwtTokenPair login(TestRestTemplate trt, int appPort, LoginCredentials credentials) {
        String url = "http://localhost:%d/api/v1/auth/login".formatted(appPort);
        return trt.postForObject(url, new LoginCredentials(credentials.getEmail(), credentials.getPassword()), JwtTokenPair.class);
    }
}
