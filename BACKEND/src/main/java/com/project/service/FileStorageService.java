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
            Files.createDirectories(tempDirPath);
            log.info("Using file storage directory at: {}", tempDirPath.toAbsolutePath());
        } catch (IOException e) {
            log.error("Failed to create temp directory: {}", tempDirPath, e);
            throw new RuntimeException("Failed to create temp directory", e);
        }
    }

    public Path saveFile(String fileName, byte[] content) {
        Path filePath = tempDirPath.resolve(fileName);
        try {
            log.info("Saving file: {}", filePath);
            Files.write(filePath, content, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            return filePath;
        } catch (IOException e) {
            log.error("Error while saving file: {}", filePath, e);
            throw new RuntimeException("Error while saving file: " + fileName, e);
        }
    }

    public Path saveFile(String fileName, InputStream stream) {
        Path filePath = tempDirPath.resolve(fileName);
        try {
            log.info("Saving file from stream: {}", filePath);
            Files.copy(stream, filePath, StandardCopyOption.REPLACE_EXISTING);
            return filePath;
        } catch (IOException e) {
            log.error("Error while saving file from stream: {}", filePath, e);
            throw new RuntimeException("Error while saving file from stream: " + fileName, e);
        }
    }

    public byte[] readFileAsBytes(String fileName) {
        Path filePath = tempDirPath.resolve(fileName);
        try {
            log.info("Reading file as bytes: {}", filePath);
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            log.error("Error while reading file as bytes: {}", filePath, e);
            throw new RuntimeException("Error while reading file as bytes: " + fileName, e);
        }
    }

    public InputStream readFileAsStream(String fileName) {
        Path filePath = tempDirPath.resolve(fileName);
        try {
            log.info("Reading file as stream: {}", filePath);
            return Files.newInputStream(filePath, StandardOpenOption.READ);
        } catch (IOException e) {
            log.error("Error while reading file as stream: {}", filePath, e);
            throw new RuntimeException("Error while reading file as stream: " + fileName, e);
        }
    }

    public boolean deleteFile(String fileName) {
        Path filePath = tempDirPath.resolve(fileName);
        try {
            boolean deleted = Files.deleteIfExists(filePath);
            log.info("File deletion {}: {}", deleted ? "successful" : "skipped (not found)", filePath);
            return deleted;
        } catch (IOException e) {
            log.error("Error while deleting file: {}", filePath, e);
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

