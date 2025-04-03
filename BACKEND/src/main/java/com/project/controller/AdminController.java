package com.project.controller;

import com.project.model.Student;
import com.project.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/student/{studentId}")
    public Student getStudentById(@PathVariable String studentId) {
        return adminService.getStudentById(studentId);
    }

    @GetMapping("/student")
    public List<Student> getStudents(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return adminService.getStudents(offset, limit);
    }

    @PostMapping("/student")
    public Student createStudent(@RequestBody Student student){return adminService.createStudent(student);}

    @PutMapping("/student/{studentId}")
    public Student editStudent(@PathVariable String studentId, @RequestBody Student student){return adminService.editStudent(studentId, student);}

}
