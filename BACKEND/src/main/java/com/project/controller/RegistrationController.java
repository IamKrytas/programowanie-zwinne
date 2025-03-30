package com.project.controller;

import com.project.model.Student;
import com.project.service.RegistrationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping("/student")
    public ResponseEntity<?> registerStudent(@RequestBody @Valid Student student) {
        try {
            Student registeredStudent = registrationService.registerStudent(student);
            return new ResponseEntity<>(registeredStudent, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
