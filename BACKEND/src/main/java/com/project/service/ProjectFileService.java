package com.project.service;

import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectFileService {
    private final FileStorageService fileStorageService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    public InputStream getProjectFile(String projectId, String fileId){
        return fileStorageService.readFileAsStream(fileId);
    }

    public Project uploadProjectFile(InputStream file, String projectId, String fileId) {
        log.info("Uploading file with ID {} for project {}", fileId, projectId);
        String fileName = "project_%s_%s".formatted(projectId, fileId);
        fileStorageService.saveFile(fileName, file);
        Project project = projectRepository.findById(projectId).orElseThrow();
        Set<String> fileSet = project.getFileIds();
        fileSet.add(fileName);
        project.setFileIds(fileSet);
        projectRepository.save(project);
        log.info("File with ID {} uploaded successfully for project {}", fileId, projectId);
        return project;
    }

    public Project deleteProjectFile(String projectId, String fileId){
        fileStorageService.deleteFile(fileId);
        Project project = projectRepository.findById(projectId).orElseThrow();
        Set<String> fileSet = project.getFileIds();
        fileSet.remove(fileId);
        project.setFileIds(fileSet);
        projectRepository.save(project);
        return project;
    }

    public InputStream getTaskFile(String taskId, String fileId){
        return fileStorageService.readFileAsStream(fileId);
    }

    public Task uploadTaskFile(InputStream file, String taskId, String fileId){
        String fileName = "task_%s_%s".formatted(taskId, fileId);
        fileStorageService.saveFile(fileName, file);
        Task task = taskRepository.findById(taskId).orElseThrow();
        Set<String> fileSet = task.getFileIds();
        fileSet.add(fileName);
        task.setFileIds(fileSet);
        task.setDoneDate(LocalDateTime.now());
        taskRepository.save(task);
        return task;
    }

    public Task deleteTaskFile(String taskId, String fileId){
        fileStorageService.deleteFile(fileId);
        Task task = taskRepository.findById(taskId).orElseThrow();
        Set<String> fileSet = task.getFileIds();
        fileSet.remove(fileId);
        task.setFileIds(fileSet);
        taskRepository.save(task);
        return task;
    }
}
