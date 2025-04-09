package com.project.service;

import com.project.model.auth.JwtTokenDetails;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.JwtTokenType;
import com.project.model.auth.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;

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
        var key = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));

        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token) // throws exception if token is invalid or expired
                .getPayload();

        var result = new JwtTokenDetails();
        result.setType(JwtTokenType.valueOf(claims.get("type", String.class)));
        result.setUserEmail(claims.getSubject());
        result.setUserRole(UserRole.valueOf(claims.get("role", String.class)));
        result.setBody(token);
        return result;
    }


    public JwtTokenPair generateTokenPair(String userEmail, UserRole role) {
        var accessToken = generateToken(userEmail, role, JwtTokenType.ACCESS, jwtAuthExpirationMs);
        var refreshToken = generateToken(userEmail, role, JwtTokenType.REFRESH, jwtRefreshExpirationMs);
        return new JwtTokenPair(accessToken, refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        var tokenDetails = validateToken(refreshToken);

        if (!tokenDetails.getType().equals(JwtTokenType.REFRESH)) {
            throw new IllegalArgumentException("Invalid token type: you must use refresh token to refresh access token");
        }

        return generateToken(tokenDetails.getUserEmail(), tokenDetails.getUserRole(), JwtTokenType.ACCESS, jwtAuthExpirationMs);
    }

    private String generateToken(String userEmail, UserRole role, JwtTokenType tokenType, long expirationTimeMs) {
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
