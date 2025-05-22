package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.model.Student;
import com.project.service.RegistrationService;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@Disabled
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RegistrationService registrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldRegisterStudentAndReturn201() throws Exception {
        Student student = new Student();
        student.setName("John");
        student.setSurname("Doe");
        student.setEmail("test@example.com");
        student.setPassword("password123");
        student.setStationary(true);

        when(registrationService.registerStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(post("/api/v1/auth/register/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void shouldReturn400IfEmailExists() throws Exception {
        Student student = new Student();
        student.setName("Jane");
        student.setSurname("Smith");
        student.setEmail("existing@example.com");
        student.setPassword("securePassword");
        student.setStationary(false);

        when(registrationService.registerStudent(any(Student.class)))
                .thenThrow(new IllegalArgumentException("Email already exists"));

        mockMvc.perform(post("/api/v1/auth/register/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student))
                        .with(csrf()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email already exists"));
    }
}


