package com.project.service;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.model.auth.JwtTokenPair;
import com.project.model.auth.LoginCredentials;
import com.project.model.auth.UserRole;
import com.project.repository.StudentRepository;
import com.project.repository.TeacherRepository;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {
    private @Mock StudentRepository studentRepository;
    private @Mock TeacherRepository teacherRepository;
    private @Mock JwtTokenService jwtService;
    private @Mock PasswordEncoder passwordEncoder;
    private @InjectMocks AuthService authService;

    private final String SAMPLE_EMAIL = "email@example.com";
    private final String SAMPLE_PASSWORD = "zaq1@WSX";
    private final String SAMPLE_ACCESS_TOKEN = "ajio9d9as8dlg9a8sd0";
    private final String SAMPLE_REFRESH_TOKEN = "asdkj9d8a9sd8f7g";

    @Test
    public void login_shouldLoginAsAdminSuccessfully() throws IllegalAccessException {
        when(jwtService.generateTokenPair(any(), any())).thenReturn(new JwtTokenPair(SAMPLE_ACCESS_TOKEN, SAMPLE_REFRESH_TOKEN));
        FieldUtils.writeField(authService, "adminDefaultEmail", SAMPLE_EMAIL, true);
        FieldUtils.writeField(authService, "adminDefaultPassword", SAMPLE_PASSWORD, true);

        var mockCredentials = new LoginCredentials(SAMPLE_EMAIL, SAMPLE_PASSWORD);
        var generatedTokens = authService.login(mockCredentials);

        assertThat(generatedTokens.getAccessToken()).isEqualTo(SAMPLE_ACCESS_TOKEN);
        assertThat(generatedTokens.getRefreshToken()).isEqualTo(SAMPLE_REFRESH_TOKEN);
        verify(jwtService).generateTokenPair(SAMPLE_EMAIL, UserRole.ADMIN);
    }

    @Test
    public void login_shouldLoginAsTeacherSuccessfully() throws IllegalAccessException {
        var mockTeacher = new Teacher("id1", "t1", "t1", SAMPLE_EMAIL, SAMPLE_PASSWORD);
        when(jwtService.generateTokenPair(any(), any())).thenReturn(new JwtTokenPair(SAMPLE_ACCESS_TOKEN, SAMPLE_REFRESH_TOKEN));
        when(teacherRepository.findByEmail(SAMPLE_EMAIL)).thenReturn(Optional.of(mockTeacher));
        when(studentRepository.findByEmail(SAMPLE_EMAIL)).thenReturn(Optional.empty());
        when(passwordEncoder.matches(SAMPLE_PASSWORD, SAMPLE_PASSWORD)).thenReturn(true);

        var generatedTokens = authService.login(new LoginCredentials(SAMPLE_EMAIL, SAMPLE_PASSWORD));
        assertThat(generatedTokens.getAccessToken()).isEqualTo(SAMPLE_ACCESS_TOKEN);
        assertThat(generatedTokens.getRefreshToken()).isEqualTo(SAMPLE_REFRESH_TOKEN);
        verify(jwtService).generateTokenPair(SAMPLE_EMAIL, UserRole.TEACHER);
    }

    @Test
    public void login_shouldLoginAsStudentSuccessfully() throws IllegalAccessException {
        var mockStudent = new Student("s1", "s1", "s1", SAMPLE_EMAIL, true, SAMPLE_PASSWORD);
        when(jwtService.generateTokenPair(any(), any())).thenReturn(new JwtTokenPair(SAMPLE_ACCESS_TOKEN, SAMPLE_REFRESH_TOKEN));
        when(studentRepository.findByEmail(SAMPLE_EMAIL)).thenReturn(Optional.of(mockStudent));
        when(passwordEncoder.matches(SAMPLE_PASSWORD, SAMPLE_PASSWORD)).thenReturn(true);

        var tokens = authService.login(new LoginCredentials(SAMPLE_EMAIL, SAMPLE_PASSWORD));
        assertThat(tokens.getAccessToken()).isEqualTo(SAMPLE_ACCESS_TOKEN);
        assertThat(tokens.getRefreshToken()).isEqualTo(SAMPLE_REFRESH_TOKEN);
        verify(jwtService).generateTokenPair(SAMPLE_EMAIL, UserRole.STUDENT);
    }

    @Test
    public void login_shouldThrowHttp401WhenCredentialsAreInvalid() {
        when(studentRepository.findByEmail(SAMPLE_EMAIL)).thenReturn(Optional.empty());
        when(teacherRepository.findByEmail(SAMPLE_EMAIL)).thenReturn(Optional.empty());

        var exception = org.junit.jupiter.api.Assertions.assertThrows(ResponseStatusException.class, () ->
            authService.login(new LoginCredentials(SAMPLE_EMAIL, SAMPLE_PASSWORD)
        ));

        assertThat(exception.getReason()).isEqualTo("Invalid credentials");
        assertThat(exception.toString()).contains("401 UNAUTHORIZED");
    }
}
