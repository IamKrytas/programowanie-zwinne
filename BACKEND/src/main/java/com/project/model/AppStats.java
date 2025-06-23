package com.project.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AppStats {
    private long totalTeachers;
    private long totalStudents;
    private long totalProjects;
    private long totalTasks;
}
