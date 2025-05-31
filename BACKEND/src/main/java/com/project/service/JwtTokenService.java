package com.project.service;

import com.project.model.auth.JwtTokenDetails;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.JwtTokenType;
import com.project.model.auth.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.util.Date;

@Slf4j
@Service
@RequiredArgsConstructor
public class JwtTokenService {
    @Value("${app.jwt-refresh-expiration-ms}")
    private long jwtRefreshExpirationMs;

    @Value("${app.jwt-auth-expiration-ms}")
    private long jwtAuthExpirationMs;

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    public JwtTokenDetails validateToken(String token) {
        log.info("Validating token");
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token) // throws exception if token is invalid or expired
                .getPayload();

        log.info("Token validated successfully for subject: {}", claims.getSubject());

        var result = new JwtTokenDetails();
        result.setType(JwtTokenType.valueOf(claims.get("type", String.class)));
        result.setUserEmail(claims.getSubject());
        result.setUserRole(UserRole.valueOf(claims.get("role", String.class)));
        result.setBody(token);
        return result;
    }

    public JwtTokenPair generateTokenPair(String userEmail, UserRole role) {
        log.info("Generating token pair for user: {}, role: {}", userEmail, role);
        var accessToken = generateToken(userEmail, role, JwtTokenType.ACCESS, jwtAuthExpirationMs);
        var refreshToken = generateToken(userEmail, role, JwtTokenType.REFRESH, jwtRefreshExpirationMs);
        return new JwtTokenPair(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        log.info("Refreshing access token using refresh token");
        var tokenDetails = validateToken(refreshToken);

        if (!tokenDetails.getType().equals(JwtTokenType.REFRESH)) {
            log.warn("Invalid token type for refresh: {}", tokenDetails.getType());
            throw new IllegalArgumentException("Invalid token type: you must use refresh token to refresh access token");
        }

        log.info("Refresh token validated, generating new access token for user: {}", tokenDetails.getUserEmail());
        return generateToken(tokenDetails.getUserEmail(), tokenDetails.getUserRole(), JwtTokenType.ACCESS, jwtAuthExpirationMs);
    }

    private String generateToken(String userEmail, UserRole role, JwtTokenType tokenType, long expirationTimeMs) {
        log.info("Generating {} token for user: {}", tokenType, userEmail);
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .subject(userEmail)
                .claim("role", role.toString())
                .claim("type", tokenType.toString())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTimeMs))
                .signWith(key)
                .compact();
    }
}

