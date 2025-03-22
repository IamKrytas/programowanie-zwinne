package com.project.model;

@Document(collection = "project")
public class Project {

    @Id
    private int id;

    private Set<int> fileId;

    private Set<int> studentId;

    @NotBlank()
    @Field(name = "teacherId")
    private int teacherId;

    private Set<int> tasks;

    private Set<int> students;

    @NotBlank()
    @Size(min = 2, max = 50, message = "Name should have at least {min} and maximum of {max} characters.")
    @Field(name = "name")
    private String name;

    @NotBlank()
    @Size(min = 10, max = 200, message = "Discription should have at least {min} and maximum of {max} characters.")
    @Field(name = "discription")
    private String discription;

    @CreatedDate
    @Field(name = "doneDate")
    private LocalDateTime doneDate;

    @CreatedDate
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

    public Set<int> getStudentId() {
        return studentId;
    }

    public void setStudentId(Set<int> studentId) {
        this.studentId = studentId;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public Set<int> getTasks() {
        return tasks;
    }

    public void setTasks(Set<int> tasks) {
        this.tasks = tasks;
    }

    public Set<int> getStudents() {
        return students;
    }

    public void setStudents(Set<int> students) {
        this.students = students;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }



}