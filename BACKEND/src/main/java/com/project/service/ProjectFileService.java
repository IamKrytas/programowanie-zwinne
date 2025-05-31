package com.project.service;

import com.project.model.Project;
import com.project.model.Task;
import com.project.repository.ProjectRepository;
import com.project.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ProjectFileService {
    private final FileStorageService fileStorageService;
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    public InputStream getProjectFile(String projectId, String fileId){
        String fileName = "project_%s_%s".formatted(projectId, fileId);
        return fileStorageService.readFileAsStream(fileName);
    }

    public Project uploadProjectFile(InputStream file, String projectId, String fileId){
        String fileName = "project_%s_%s".formatted(projectId, fileId);
        fileStorageService.saveFile(fileName, file);
        Project project = projectRepository.findById(projectId).orElseThrow();
        Set<String> fileSet = project.getFileId();
        fileSet.add(fileName);
        project.setFileId(fileSet);
        projectRepository.save(project);
        return project;
    }

    public Project deleteProjectFile(String projectId, String fileId){
        String fileName = "project_%s_%s".formatted(projectId, fileId);
        fileStorageService.deleteFile(fileName);
        Project project = projectRepository.findById(projectId).orElseThrow();
        Set<String> fileSet = project.getFileId();
        fileSet.remove(fileName);
        project.setFileId(fileSet);
        projectRepository.save(project);
        return project;
    }

    public InputStream getTaskFile(String taskId, String fileId){
        String fileName = "task_%s_%s".formatted(taskId, fileId);
        return fileStorageService.readFileAsStream(fileName);
    }

    public Task uploadTaskFile(InputStream file, String taskId, String fileId){
        String fileName = "task_%s_%s".formatted(taskId, fileId);
        fileStorageService.saveFile(fileName, file);
        Task task = taskRepository.findById(taskId).orElseThrow();
        Set<String> fileSet = task.getFileId();
        fileSet.add(fileName);
        task.setFileId(fileSet);
        taskRepository.save(task);
        return task;
    }

    public Task deleteTaskFile(String taskId, String fileId){
        String fileName = "task_%s_%s".formatted(taskId, fileId);
        fileStorageService.deleteFile(fileName);
        Task task = taskRepository.findById(taskId).orElseThrow();
        Set<String> fileSet = task.getFileId();
        fileSet.remove(fileName);
        task.setFileId(fileSet);
        taskRepository.save(task);
        return task;
    }
}
