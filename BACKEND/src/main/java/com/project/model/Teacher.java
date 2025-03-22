package com.project.model;

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
    private String emial;

    @NotBlank()
    @Size(min = 18, max = 50, message = "Password should have at least {min}.")
    @Field(name = "password")
    private String password;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmial() {
        return emial;
    }

    public void setEmial(String emial) {
        this.emial = emial;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }


}