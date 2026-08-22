package com.example.projectmanagement.dto;

import com.example.projectmanagement.model.Project;
import com.example.projectmanagement.model.ProjectStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectDtoTest {

    @Test
    @DisplayName("Test ProjectRequest getters, setters and constructors")
    void testProjectRequest() {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        ProjectRequest request = new ProjectRequest();
        request.setName("Req Project");
        request.setDescription("Req Desc");
        request.setOwner("Owner");
        request.setStatus(ProjectStatus.PLANNED);
        request.setStartDate(startDate);
        request.setEndDate(endDate);

        assertThat(request.getName()).isEqualTo("Req Project");
        assertThat(request.getDescription()).isEqualTo("Req Desc");
        assertThat(request.getOwner()).isEqualTo("Owner");
        assertThat(request.getStatus()).isEqualTo(ProjectStatus.PLANNED);
        assertThat(request.getStartDate()).isEqualTo(startDate);
        assertThat(request.getEndDate()).isEqualTo(endDate);

        ProjectRequest request2 = new ProjectRequest("Req Project", "Req Desc", "Owner", ProjectStatus.PLANNED, startDate, endDate);
        assertThat(request2.getName()).isEqualTo("Req Project");
    }

    @Test
    @DisplayName("Test ProjectResponse getters, setters and fromEntity static factory method")
    void testProjectResponse() {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        Project project = new Project(5L, "Resp Project", "Resp Desc", "Owner", ProjectStatus.COMPLETED, startDate, endDate);
        ProjectResponse response = ProjectResponse.fromEntity(project);

        assertThat(response.getId()).isEqualTo(5L);
        assertThat(response.getName()).isEqualTo("Resp Project");
        assertThat(response.getDescription()).isEqualTo("Resp Desc");
        assertThat(response.getOwner()).isEqualTo("Owner");
        assertThat(response.getStatus()).isEqualTo(ProjectStatus.COMPLETED);
        assertThat(response.getStartDate()).isEqualTo(startDate);
        assertThat(response.getEndDate()).isEqualTo(endDate);

        ProjectResponse response2 = new ProjectResponse();
        response2.setId(10L);
        response2.setName("Name");
        response2.setDescription("Desc");
        response2.setOwner("Own");
        response2.setStatus(ProjectStatus.ON_HOLD);
        response2.setStartDate(startDate);
        response2.setEndDate(endDate);

        assertThat(response2.getId()).isEqualTo(10L);
        assertThat(response2.getStatus()).isEqualTo(ProjectStatus.ON_HOLD);
    }
}
