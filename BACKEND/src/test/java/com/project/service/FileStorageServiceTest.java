package com.project.service;

import org.junit.jupiter.api.*;
import java.io.*;
import java.nio.file.*;
import static org.junit.jupiter.api.Assertions.*;

class FileStorageServiceTest {
    private static final FileStorageService fileStorageService = new FileStorageService();
    @BeforeAll static void setup() {
        fileStorageService.init();
    }

    @Test
    void test_saveAndReadFileAsBytes() {
        String fileName = "ut__test.txt";
        byte[] content = "Hello, Koala!".getBytes();

        Path savedPath = fileStorageService.saveFile(fileName, content);
        assertTrue(Files.exists(savedPath));

        byte[] readBytes = fileStorageService.readFileAsBytes(fileName);
        assertArrayEquals(content, readBytes);
    }

    @Test
    void test_saveAndReadFileAsStream() throws IOException {
        String fileName = "ut__stream.txt";
        byte[] content = "Stream content!".getBytes();

        try (InputStream input = new ByteArrayInputStream(content)) {
            Path savedPath = fileStorageService.saveFile(fileName, input);
            assertTrue(Files.exists(savedPath));
        }

        try (InputStream readStream = fileStorageService.readFileAsStream(fileName)) {
            byte[] readBytes = readStream.readAllBytes();
            assertArrayEquals(content, readBytes);
        }
    }

    @Test
    void test_deleteFile() {
        String fileName = "ut__delete-me.txt";
        byte[] content = "Goodbye!".getBytes();
        fileStorageService.saveFile(fileName, content);

        boolean deleted = fileStorageService.deleteFile(fileName);
        assertTrue(deleted);
        assertFalse(Files.exists(fileStorageService.getFilePath(fileName)));
    }

    @Test
    void test_getFilePath() {
        String fileName = "ut__example.txt";
        Path expected = fileStorageService.getFilePath(fileName);
        assertEquals(expected.toString(), fileStorageService.getFilePath(fileName).toString());
    }
}
