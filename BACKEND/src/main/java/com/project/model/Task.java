package com.project.model;

import jakarta.validation.constraints.NotBlank;
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
    private int id;

    @NotBlank()
    @Field(name = "fileId")
    private Set<Integer> fileId;

    @NotBlank()
    @Field(name = "studentId")
    private int studentId;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank()
    @Size(min = 10, max = 200, message = "Description should have at least {min} and maximum of {max} characters.")
    @Field(name = "description")
    private String description;

    @NotBlank()
    @Field(name = "priority")
    private int priority; //1 - 10

    @NotBlank()
    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @NotBlank()
    @Field(name = "creationDate")
    private LocalDateTime creationDate;

}