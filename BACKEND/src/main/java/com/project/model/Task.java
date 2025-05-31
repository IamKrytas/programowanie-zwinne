package com.project.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "task")
public class Task {

    @Id
    private String id;

    @Field(name = "projectId")
    private String projectId;

    @Field(name = "teacherId")
    private String teacherId;

    @NotEmpty(message = "fileIds cannot be empty")
    @Field(name = "fileIds")
    private Set<String> fileIds;

    @NotBlank
    @Field(name = "assignedStudentId")
    private String assignedStudentId;

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Field(name = "description")
    private String description;

    @Min(1)
    @Field(name = "priority")
    private int priority;

    @NotNull
    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @NotNull
    @Field(name = "creationDate")
    private LocalDateTime creationDate;
}