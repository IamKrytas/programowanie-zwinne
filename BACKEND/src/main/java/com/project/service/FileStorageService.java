package com.project.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

@Slf4j
@Service
public class FileStorageService {

    private static final String TEMP_DIR_NAME = "koala-app-storage";
    private Path tempDirPath;

    @PostConstruct
    public void init() {
        String baseTempDir = System.getProperty("java.io.tmpdir");
        tempDirPath = Paths.get(baseTempDir, TEMP_DIR_NAME);
        try {
            log.info("Using file storage directory at: {}", tempDirPath.toAbsolutePath());
            Files.createDirectories(tempDirPath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    public Path saveFile(String fileName, byte[] content) {
        try {
            Path filePath = tempDirPath.resolve(fileName);
            Files.write(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file: " + fileName, e);
        }
    }

    public Path saveFile(String fileName, InputStream stream) {
        try {
            Path filePath = tempDirPath.resolve(fileName);
            Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            throw new RuntimeException("Error while saving file from stream: " + fileName, e);
        }
    }

    public byte[] readFileAsBytes(String fileName) {
        try {
            Path filePath = tempDirPath.resolve(fileName);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file as bytes: " + fileName, e);
        }
    }

    public InputStream readFileAsStream(String fileName) {
        try {
            Path filePath = tempDirPath.resolve(fileName);
            return Files.newInputStream(filePath, StandardOpenOption.READ);
        } catch (IOException e) {
            throw new RuntimeException("Error while reading file as stream: " + fileName, e);
        }
    }

    public boolean deleteFile(String fileName) {
        try {
            return Files.deleteIfExists(tempDirPath.resolve(fileName));
        } catch (IOException e) {
            throw new RuntimeException("Error while deleting file: " + fileName, e);
        }
    }

    public Path getStorageDirectoryPath() {
        return tempDirPath;
    }

    public Path getFilePath(String fileName) {
        return tempDirPath.resolve(fileName);
    }
}
