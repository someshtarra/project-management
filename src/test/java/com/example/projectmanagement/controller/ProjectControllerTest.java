package com.example.projectmanagement.controller;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.model.ProjectStatus;
import com.example.projectmanagement.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.BDDMockito.willThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProjectController.class)
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProjectResponse sampleResponse;
    private ProjectRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleResponse = new ProjectResponse(
                1L,
                "Auth Microservice",
                "User authentication & authorization",
                "John Doe",
                ProjectStatus.PLANNED,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 8, 31)
        );

        sampleRequest = new ProjectRequest(
                "Auth Microservice",
                "User authentication & authorization",
                "John Doe",
                ProjectStatus.PLANNED,
                LocalDate.of(2026, 3, 1),
                LocalDate.of(2026, 8, 31)
        );
    }

    @Test
    @DisplayName("GET /api/projects should return 200 OK and list of projects")
    void getAllProjects_shouldReturnListOfProjects() throws Exception {
        given(projectService.getAllProjects()).willReturn(List.of(sampleResponse));

        mockMvc.perform(get("/api/projects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Auth Microservice")))
                .andExpect(jsonPath("$[0].owner", is("John Doe")));
    }

    @Test
    @DisplayName("GET /api/projects/{id} should return 200 OK when project exists")
    void getProjectById_whenProjectExists_shouldReturn200() throws Exception {
        given(projectService.getProjectById(1L)).willReturn(sampleResponse);

        mockMvc.perform(get("/api/projects/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Auth Microservice")));
    }

    @Test
    @DisplayName("GET /api/projects/{id} should return 404 NOT FOUND when project missing")
    void getProjectById_whenProjectMissing_shouldReturn404() throws Exception {
        given(projectService.getProjectById(99L))
                .willThrow(new ResourceNotFoundException("Project not found with id: 99"));

        mockMvc.perform(get("/api/projects/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Project not found with id: 99")));
    }

    @Test
    @DisplayName("POST /api/projects should return 201 CREATED for valid payload")
    void createProject_whenValidPayload_shouldReturn201() throws Exception {
        given(projectService.createProject(any(ProjectRequest.class))).willReturn(sampleResponse);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Auth Microservice")));
    }

    @Test
    @DisplayName("POST /api/projects should return 400 BAD REQUEST when required fields missing")
    void createProject_whenInvalidPayload_shouldReturn400() throws Exception {
        ProjectRequest invalidRequest = new ProjectRequest("", "", "", null, null, null);

        mockMvc.perform(post("/api/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.validationErrors.name", is("Project name is required")))
                .andExpect(jsonPath("$.validationErrors.owner", is("Owner is required")))
                .andExpect(jsonPath("$.validationErrors.status", is("Status is required")));
    }

    @Test
    @DisplayName("PUT /api/projects/{id} should return 200 OK for valid update")
    void updateProject_whenValid_shouldReturn200() throws Exception {
        given(projectService.updateProject(eq(1L), any(ProjectRequest.class))).willReturn(sampleResponse);

        mockMvc.perform(put("/api/projects/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Auth Microservice")));
    }

    @Test
    @DisplayName("DELETE /api/projects/{id} should return 204 NO CONTENT on successful deletion")
    void deleteProject_whenValidId_shouldReturn204() throws Exception {
        willDoNothing().given(projectService).deleteProject(1L);

        mockMvc.perform(delete("/api/projects/1"))
                .andExpect(status().isNoContent());
    }
}
