package com.project.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "teacher")
public class Teacher {

    @Id
    private int id;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Surname should have at least {min} and maximum of {max} characters.")
    @Field(name = "surname")
    private String surname;

    @NotBlank()
    @Size(min = 2, max = 50, message = "E-mail should have at least {min} and maximum of {max} characters.")
    @Field(name = "email")
    private String email;

    @NotBlank()
    @Size(min = 18, max = 50, message = "Password should have at least {min}.")
    @Field(name = "password")
    private String password;

}