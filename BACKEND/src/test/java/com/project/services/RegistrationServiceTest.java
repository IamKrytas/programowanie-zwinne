package com.project.services;

import com.project.model.Student;
import com.project.repositories.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    private RegistrationService registrationService;
    private StudentRepository studentRepository;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        // Mockowanie zależności
        studentRepository = mock(StudentRepository.class);
        passwordEncoder = mock(BCryptPasswordEncoder.class);

        // Tworzenie instancji serwisu, który będzie testowany
        registrationService = new RegistrationService(studentRepository, passwordEncoder);
    }

    @Test
    void shouldRegisterStudentSuccessfully() {
        // Przygotowanie obiektu Student
        Student student = new Student();
        student.setEmail("test@example.com");
        student.setPassword("plainPassword");

        // Mockowanie, że email nie istnieje w bazie
        when(studentRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        // Mockowanie zachowania enkodera hasła
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        // Mockowanie zapisania studenta
        when(studentRepository.save(any(Student.class))).thenAnswer(i -> i.getArgument(0));

        // Rejestracja studenta
        Student registered = registrationService.registerStudent(student);

        // Asercje
        assertThat(registered).isNotNull();
        assertThat(registered.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        // Przygotowanie studenta z duplikatem e-maila
        Student student = new Student();
        student.setEmail("duplicate@example.com");

        // Mockowanie, że email już istnieje w bazie
        when(studentRepository.findByEmail("duplicate@example.com")).thenReturn(Optional.of(new Student()));

        // Testowanie wyjątku
        assertThatThrownBy(() -> registrationService.registerStudent(student))
            .isInstanceOf(IllegalArgumentException.class) // Sprawdzenie typu wyjątku
            .hasMessageContaining("Email already exists"); // Sprawdzenie komunikatu
    }
}

