package com.project.model.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * LoginCredentials is a class that represents the credentials used for logging in.
 * It is common for student, teacher and admin accounts.
 */
@Data @AllArgsConstructor @NoArgsConstructor
public class LoginCredentials {
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
