package com.project.controller;

import com.project.model.Student;
import com.project.model.Teacher;
import com.project.service.ListUsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/list")
public class ListUsersController {
    private final ListUsersService listStudentsService;

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return listStudentsService.getAllStudents();
    }

    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
        return listStudentsService.getAllTeachers();
    }
}
