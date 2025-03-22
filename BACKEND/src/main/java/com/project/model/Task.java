package com.project.model;

@Document(collection = "task")
public class Task {

    @Id
    private int id;

    private Set<int> fileId;

    @NotBlank()
    @Field(name = "studentId")
    private int studentId;

    @NotBlank()
    @Field(name = "projectId")
    private int projectId;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank()
    @Size(min = 10, max = 200, message = "Discription should have at least {min} and maximum of {max} characters.")
    @Field(name = "discription")
    private String discription;

    @NotBlank()
    @Field(name = "priority")
    private int priority; //1 - 10

    @NotBlank()
    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @NotBlank()
    @Field(name = "creationDate")
    private LocalDateTime creationDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Set<int> getFileId() {
        return fileId;
    }

    public void setFileId(Set<int> fileId) {
        this.fileId = fileId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public LocalDateTime getDoneDate() {
        return doneDate;
    }

    public void setDoneDate(LocalDateTime doneDate) {
        this.doneDate = doneDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }



}