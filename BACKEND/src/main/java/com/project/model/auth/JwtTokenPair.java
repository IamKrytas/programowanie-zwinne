package com.project.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a pair of JWT tokens: an access token and a refresh token.
 * It is used to return issued tokens to the client after successful authentication.
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class JwtTokenPair {
    private String accessToken;
    private String refreshToken;
}
