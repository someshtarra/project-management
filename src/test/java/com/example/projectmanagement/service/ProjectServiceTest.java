package com.example.projectmanagement.service;

import com.example.projectmanagement.dto.ProjectRequest;
import com.example.projectmanagement.dto.ProjectResponse;
import com.example.projectmanagement.exception.ResourceNotFoundException;
import com.example.projectmanagement.model.Project;
import com.example.projectmanagement.model.ProjectStatus;
import com.example.projectmanagement.repository.ProjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {

    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    private Project sampleProject;
    private ProjectRequest sampleRequest;

    @BeforeEach
    void setUp() {
        sampleProject = new Project(
                1L,
                "Payment Service",
                "Build payment integration",
                "Jane Doe",
                ProjectStatus.IN_PROGRESS,
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2026, 6, 30)
        );

        sampleRequest = new ProjectRequest(
                "Payment Service",
                "Build payment integration",
                "Jane Doe",
                ProjectStatus.IN_PROGRESS,
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2026, 6, 30)
        );
    }

    @Test
    @DisplayName("Should return list of projects when getAllProjects is called")
    void getAllProjects_shouldReturnProjectList() {
        given(projectRepository.findAll()).willReturn(List.of(sampleProject));

        List<ProjectResponse> result = projectService.getAllProjects();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Payment Service");
        assertThat(result.get(0).getOwner()).isEqualTo("Jane Doe");
    }

    @Test
    @DisplayName("Should return project response when valid ID is provided")
    void getProjectById_whenValidId_shouldReturnProject() {
        given(projectRepository.findById(1L)).willReturn(Optional.of(sampleProject));

        ProjectResponse response = projectService.getProjectById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getName()).isEqualTo("Payment Service");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when project ID is not found")
    void getProjectById_whenInvalidId_shouldThrowException() {
        given(projectRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> projectService.getProjectById(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Project not found with id: 99");
    }

    @Test
    @DisplayName("Should create and save project successfully")
    void createProject_shouldReturnSavedProject() {
        given(projectRepository.save(any(Project.class))).willReturn(sampleProject);

        ProjectResponse response = projectService.createProject(sampleRequest);

        assertThat(response).isNotNull();
        assertThat(response.getName()).isEqualTo("Payment Service");
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    @DisplayName("Should update existing project successfully")
    void updateProject_whenValidId_shouldUpdateAndReturnProject() {
        given(projectRepository.findById(1L)).willReturn(Optional.of(sampleProject));
        given(projectRepository.save(any(Project.class))).willReturn(sampleProject);

        ProjectRequest updateRequest = new ProjectRequest(
                "Payment Service Updated",
                "Updated description",
                "Jane Doe",
                ProjectStatus.COMPLETED,
                LocalDate.of(2026, 1, 15),
                LocalDate.of(2026, 5, 30)
        );

        ProjectResponse response = projectService.updateProject(1L, updateRequest);

        assertThat(response).isNotNull();
        verify(projectRepository).save(sampleProject);
    }

    @Test
    @DisplayName("Should delete project successfully when ID exists")
    void deleteProject_whenValidId_shouldDeleteProject() {
        given(projectRepository.findById(1L)).willReturn(Optional.of(sampleProject));
        willDoNothing().given(projectRepository).delete(sampleProject);

        projectService.deleteProject(1L);

        verify(projectRepository).delete(sampleProject);
    }
}
