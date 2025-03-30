package com.project.controller;

import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import com.project.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    private @Mock AuthService authService;

    private @InjectMocks AuthController authController;

    @Test
    void handleLogin_shouldReturnTokensIfCredentialsAreValid() {
        LoginCredentials credentials = new LoginCredentials("user@example.com", "password123");
        JwtTokenPair expectedTokens = new JwtTokenPair("access-token", "refresh-token");
        when(authService.login(credentials)).thenReturn(expectedTokens);

        JwtTokenPair actualTokens = authController.handleLogin(credentials);
        assertEquals(expectedTokens, actualTokens);
        verify(authService).login(credentials);
    }

    @Test
    void handleLogin_shouldRaiseHttp401IfCredentialsAreInvalid() {
        LoginCredentials credentials = new LoginCredentials("user@example.com", "password123");
        when(authService.login(credentials)).thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            authController.handleLogin(credentials));
        assertThat(exception).hasMessageContaining("401 UNAUTHORIZED");
    }

    @Test
    void handleTokenRefresh_shouldReturnNewAccessTokenIfRefreshTokenIsValid() {
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        when(authService.refreshAccessToken(refreshToken)).thenReturn(newAccessToken);

        String result = authController.handleTokenRefresh(refreshToken);
        assertEquals(newAccessToken, result);
        verify(authService).refreshAccessToken(refreshToken);
    }

    @Test
    void handleTokenRefresh_shouldRaiseHttp400IfRefreshTokenIsInvalid() {
        String refreshToken = "valid-refresh-token";
        String newAccessToken = "new-access-token";
        when(authService.refreshAccessToken(refreshToken)).thenThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
            authController.handleTokenRefresh(refreshToken));
        assertThat(exception).hasMessageContaining("400 BAD_REQUEST");
    }
}
