package com.project.service;

import com.project.model.auth.JwtTokenType;
import com.project.model.auth.UserRole;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
public class JwtTokenServiceTest {
    @InjectMocks
    private JwtTokenService jwtTokenService;

    private final String EXAMPLE_SECRET = "y6cmo3yrch0uil79t0fwhj0mt3b8534k";
    private final String EXAMPLE_EMAIL = "email@example.com";

    @Test
    public void generateTokenPair_adminTokens() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);

        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.ADMIN);
        var parsedAccessToken = jwtTokenService.validateToken(tokenPair.getAccessToken());
        var parsedRefreshToken = jwtTokenService.validateToken(tokenPair.getRefreshToken());

        assertThat(parsedAccessToken.getType()).isEqualTo(JwtTokenType.ACCESS);
        assertThat(parsedAccessToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedAccessToken.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(parsedAccessToken.getBody()).isEqualTo(tokenPair.getAccessToken());
        assertThat(parsedRefreshToken.getType()).isEqualTo(JwtTokenType.REFRESH);
        assertThat(parsedRefreshToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedRefreshToken.getUserRole()).isEqualTo(UserRole.ADMIN);
        assertThat(parsedRefreshToken.getBody()).isEqualTo(tokenPair.getRefreshToken());
    }

    @Test
    public void generateTokenPair_studentTokens() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);

        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.STUDENT);
        var parsedAccessToken = jwtTokenService.validateToken(tokenPair.getAccessToken());
        var parsedRefreshToken = jwtTokenService.validateToken(tokenPair.getRefreshToken());

        assertThat(parsedAccessToken.getType()).isEqualTo(JwtTokenType.ACCESS);
        assertThat(parsedAccessToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedAccessToken.getUserRole()).isEqualTo(UserRole.STUDENT);
        assertThat(parsedAccessToken.getBody()).isEqualTo(tokenPair.getAccessToken());
        assertThat(parsedRefreshToken.getType()).isEqualTo(JwtTokenType.REFRESH);
        assertThat(parsedRefreshToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedRefreshToken.getUserRole()).isEqualTo(UserRole.STUDENT);
        assertThat(parsedRefreshToken.getBody()).isEqualTo(tokenPair.getRefreshToken());

    }

    @Test
    public void generateTokenPair_teacherTokens() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);

        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.TEACHER);
        var parsedAccessToken = jwtTokenService.validateToken(tokenPair.getAccessToken());
        var parsedRefreshToken = jwtTokenService.validateToken(tokenPair.getRefreshToken());

        assertThat(parsedAccessToken.getType()).isEqualTo(JwtTokenType.ACCESS);
        assertThat(parsedAccessToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedAccessToken.getUserRole()).isEqualTo(UserRole.TEACHER);
        assertThat(parsedAccessToken.getBody()).isEqualTo(tokenPair.getAccessToken());
        assertThat(parsedRefreshToken.getType()).isEqualTo(JwtTokenType.REFRESH);
        assertThat(parsedRefreshToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedRefreshToken.getUserRole()).isEqualTo(UserRole.TEACHER);
        assertThat(parsedRefreshToken.getBody()).isEqualTo(tokenPair.getRefreshToken());

    }

    @Test
    public void refreshAccessToken_shouldReturnNewToken() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.TEACHER);
        var newAccessToken = jwtTokenService.refreshAccessToken(tokenPair.getRefreshToken());
        var parsedNewAccessToken = jwtTokenService.validateToken(newAccessToken);

        assertThat(parsedNewAccessToken.getType()).isEqualTo(JwtTokenType.ACCESS);
        assertThat(parsedNewAccessToken.getUserEmail()).isEqualTo(EXAMPLE_EMAIL);
        assertThat(parsedNewAccessToken.getUserRole()).isEqualTo(UserRole.TEACHER);
        assertThat(parsedNewAccessToken.getBody()).isEqualTo(newAccessToken);
    }

    @Test
    public void refreshAccessToken_shouldThrowExceptionWhenTokenTypeIsNotRefresh() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.TEACHER);

        assertThatThrownBy(() -> jwtTokenService.refreshAccessToken(tokenPair.getAccessToken()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid token type: you must use refresh token to refresh access token");
    }

    @Test
    public void refreshAccessToken_shouldThrowExceptionWhenTokenIsExpired()
            throws IllegalAccessException, InterruptedException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 1, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 1, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.TEACHER);
        Thread.sleep(10); // wait for the token to expire
        assertThatThrownBy(() -> jwtTokenService.refreshAccessToken(tokenPair.getRefreshToken()))
                .isInstanceOf(ExpiredJwtException.class)
                .hasMessageContaining("JWT expired");
    }

    @Test
    public void refreshAccessToken_shouldThrowExceptionWhenTokenIsMalformed()
            throws IllegalAccessException, InterruptedException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        assertThatThrownBy(() -> jwtTokenService.refreshAccessToken("dummy-data"))
                .isInstanceOf(MalformedJwtException.class)
                .hasMessageContaining("Invalid compact JWT string");
    }

    @Test
    public void validateToken_shouldThrowExceptionWhenTokenIsMalformed() throws IllegalAccessException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 3600000, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 604800000L, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        assertThatThrownBy(() -> jwtTokenService.validateToken("dummy-data"))
                .isInstanceOf(MalformedJwtException.class)
                .hasMessageContaining("Invalid compact JWT string");
    }

    @Test
    public void validateToken_shouldThrowExceptionWhenTokenIsExpired() throws IllegalAccessException, InterruptedException {
        FieldUtils.writeField(jwtTokenService, "jwtAuthExpirationMs", 1, true);
        FieldUtils.writeField(jwtTokenService, "jwtRefreshExpirationMs", 1, true);
        FieldUtils.writeField(jwtTokenService, "jwtSecret", EXAMPLE_SECRET, true);
        var tokenPair = jwtTokenService.generateTokenPair(EXAMPLE_EMAIL, UserRole.TEACHER);
        Thread.sleep(10); // wait for the token to expire
        assertThatThrownBy(() -> jwtTokenService.validateToken(tokenPair.getAccessToken()))
                .isInstanceOf(ExpiredJwtException.class)
                .hasMessageContaining("JWT expired");
    }
}
