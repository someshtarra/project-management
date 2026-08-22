package com.example.projectmanagement;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ProjectManagementApplicationTest {

    @Test
    @DisplayName("Context Loads Test")
    void contextLoads() {
        // Verifies Spring context loads successfully
    }

    @Test
    @DisplayName("Main method execution test")
    void mainMethodTest() {
        ProjectManagementApplication.main(new String[] {});
    }
}
