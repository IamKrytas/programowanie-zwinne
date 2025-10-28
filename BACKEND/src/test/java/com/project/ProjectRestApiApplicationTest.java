package com.project;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.profiles.active=test")
public class ProjectRestApiApplicationTest {
    @Test
    public void contextLoads() {
    }
}
