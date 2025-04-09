package com.project.model.auth;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtTokenDetails {
    private JwtTokenType type;
    private String userEmail;
    private UserRole userRole;
    private String body;
}
