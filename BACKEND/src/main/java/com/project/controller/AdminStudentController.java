package com.project.controller;

import com.project.model.Student;
import com.project.service.AdminStudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminStudentController {
    private final AdminStudentService adminStudentService;

    @GetMapping("/student/{studentId}")
    public Student getStudentById(@PathVariable String studentId) {
        return adminStudentService.getStudentById(studentId);
    }

    @GetMapping("/student")
    public List<Student> getStudents(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return adminStudentService.getStudents(offset, limit);
    }

    @PostMapping("/student")
    public Student createStudent(@RequestBody @Valid Student student){return adminStudentService.createStudent(student);}

    @PutMapping("/student/{studentId}")
    public Student editStudent(@PathVariable String studentId, @RequestBody @Valid Student student ){return adminStudentService.editStudent(studentId, student);}

    @DeleteMapping("/student/{studentId}")
    public Student deleteStudent(@PathVariable String studentId){return adminStudentService.deleteStudent(studentId);}

}
