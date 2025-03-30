package com.project.model.auth;

/**
 * Enum representing the type of JWT token.
 * JWT token issued by the server can be of two types:
 * 1. ACCESS - used for authorization to access protected API endpoints,
 * 2. REFRESH - used to obtain a new access token when the current access token expires.
 */
public enum JwtTokenType {
    ACCESS,
    REFRESH
}
