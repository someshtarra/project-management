package com.example.projectmanagement.dto;

import com.example.projectmanagement.model.Project;
import com.example.projectmanagement.model.ProjectStatus;

import java.time.LocalDate;

public class ProjectResponse {

    private Long id;
    private String name;
    private String description;
    private String owner;
    private ProjectStatus status;
    private LocalDate startDate;
    private LocalDate endDate;

    public ProjectResponse() {
    }

    public ProjectResponse(Long id, String name, String description, String owner, ProjectStatus status, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.owner = owner;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static ProjectResponse fromEntity(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner(),
                project.getStatus(),
                project.getStartDate(),
                project.getEndDate()
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public ProjectStatus getStatus() {
        return status;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
