package com.example.projectmanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProjectManagementApplicationTest {

    @Test
    @DisplayName("Context Loads Test")
    void contextLoads() {
        // Verifies Spring context loads successfully without binding to port 8080
    }
}
