package com.project.controller;

import com.project.model.Teacher;
import com.project.service.AdminTeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminTeacherController {
    private final AdminTeacherService adminTeacherService;

    @GetMapping("/teacher/{teacherId}")
    public Teacher getTeacherById(@PathVariable String teacherId) {
        return adminTeacherService.getTeacherById(teacherId);
    }

    @GetMapping("/teacher")
    public List<Teacher> getTeachers(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return adminTeacherService.getTeachers(offset, limit);
    }

    @PostMapping("/teacher")
    public Teacher createTeacher(@RequestBody Teacher teacher){return adminTeacherService.createTeacher(teacher);}

    @PutMapping("/teacher/{teacherId}")
    public Teacher editTeacher(@PathVariable String teacherId, @RequestBody Teacher teacher){return adminTeacherService.editTeacher(teacherId, teacher);}

    @DeleteMapping("/teacher/{teacherId}")
    public Teacher deleteTeacher(@PathVariable String teacherId){return adminTeacherService.deleteStudent(teacherId);}

}
