package com.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "project")
public class Project {
    @Id
    private String id;

    @NotEmpty(message = "fileIds cannot be empty")
    @Field(name = "fileIds")
    private Set<String> fileIds;

    @NotBlank(message = "teacherId cannot be blank")
    @Field(name = "teacherId")
    private String teacherId;

    @NotEmpty(message = "tasks cannot be empty")
    @Field(name = "tasks")
    private Set<Task> tasks;

    @NotEmpty(message = "students cannot be empty")
    @Field(name = "students")
    private Set<String> studentIds;

    @NotBlank
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Field(name = "description")
    private String description;

    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @CreatedDate
    @Field(name = "creationDate")
    private LocalDateTime creationDate;
}
