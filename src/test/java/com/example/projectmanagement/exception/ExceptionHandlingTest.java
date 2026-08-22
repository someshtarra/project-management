package com.example.projectmanagement.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.time.LocalDateTime;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class ExceptionHandlingTest {

    @Test
    @DisplayName("Test ErrorDetails getters and setters")
    void testErrorDetails() {
        LocalDateTime now = LocalDateTime.now();
        ErrorDetails error = new ErrorDetails(now, 404, "Not Found", "Project missing", "/api/projects/99");

        assertThat(error.getTimestamp()).isEqualTo(now);
        assertThat(error.getStatus()).isEqualTo(404);
        assertThat(error.getError()).isEqualTo("Not Found");
        assertThat(error.getMessage()).isEqualTo("Project missing");
        assertThat(error.getPath()).isEqualTo("/api/projects/99");

        ErrorDetails error2 = new ErrorDetails();
        error2.setTimestamp(now);
        error2.setStatus(400);
        error2.setError("Bad Request");
        error2.setMessage("Invalid input");
        error2.setPath("/api/projects");
        error2.setValidationErrors(Map.of("field", "error"));

        assertThat(error2.getStatus()).isEqualTo(400);
        assertThat(error2.getValidationErrors()).containsEntry("field", "error");
    }

    @Test
    @DisplayName("Test GlobalExceptionHandler handleGlobalException for unhandled exceptions")
    void testHandleGlobalException() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRequestURI("/api/projects");

        Exception ex = new RuntimeException("Unexpected error");
        var response = handler.handleGlobalException(ex, request);

        assertThat(response.getStatusCodeValue()).isEqualTo(500);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Unexpected error");
    }
}
