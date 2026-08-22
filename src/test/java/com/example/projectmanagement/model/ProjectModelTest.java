package com.example.projectmanagement.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class ProjectModelTest {

    @Test
    @DisplayName("Test Project entity getters, setters, constructors, equals and hashCode")
    void testProjectEntity() {
        LocalDate startDate = LocalDate.of(2026, 1, 1);
        LocalDate endDate = LocalDate.of(2026, 12, 31);

        Project project1 = new Project();
        project1.setId(1L);
        project1.setName("Test Project");
        project1.setDescription("Test Description");
        project1.setOwner("Alice");
        project1.setStatus(ProjectStatus.IN_PROGRESS);
        project1.setStartDate(startDate);
        project1.setEndDate(endDate);

        assertThat(project1.getId()).isEqualTo(1L);
        assertThat(project1.getName()).isEqualTo("Test Project");
        assertThat(project1.getDescription()).isEqualTo("Test Description");
        assertThat(project1.getOwner()).isEqualTo("Alice");
        assertThat(project1.getStatus()).isEqualTo(ProjectStatus.IN_PROGRESS);
        assertThat(project1.getStartDate()).isEqualTo(startDate);
        assertThat(project1.getEndDate()).isEqualTo(endDate);

        Project project2 = new Project(1L, "Test Project", "Test Description", "Alice", ProjectStatus.IN_PROGRESS, startDate, endDate);

        assertThat(project1).isEqualTo(project2);
        assertThat(project1.hashCode()).isEqualTo(project2.hashCode());
        assertThat(project1).isEqualTo(project1);
        assertThat(project1).isNotEqualTo(null);
        assertThat(project1).isNotEqualTo("other object");

        Project project3 = new Project(2L, "Different Project", "Desc", "Bob", ProjectStatus.PLANNED, startDate, endDate);
        assertThat(project1).isNotEqualTo(project3);
    }
}
