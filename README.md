# Project Management Web Application

A complete, production-grade Spring Boot 3 & Java 21 RESTful web application built with Maven for managing software projects. This project serves as a clean, standardized foundation for Java development, Spring Boot learning, and future CI/CD pipeline automation (such as Docker, Jenkins, Kubernetes, and Helm).

---

## Table of Contents
1. [Overview & Tech Stack](#overview--tech-stack)
2. [Project Architecture & File Details](#project-architecture--file-details)
3. [Prerequisites](#prerequisites)
4. [Maven Build & Run Commands](#maven-build--run-commands)
5. [Domain Model & Validation](#domain-model--validation)
6. [REST API Documentation](#rest-api-documentation)
7. [Error Handling & Validation Responses](#error-handling--validation-responses)
8. [Database Configuration (H2 & PostgreSQL)](#database-configuration-h2--postgresql)
9. [Frontend Web Dashboard](#frontend-web-dashboard)
10. [Testing Suite](#testing-suite)
11. [CI/CD Readiness & Future Artifacts](#cicd-readiness--future-artifacts)

---

## Overview & Tech Stack

- **Java**: Java 21 (LTS)
- **Framework**: Spring Boot 3.3.4
- **Build Tool**: Apache Maven
- **Persistence**: Spring Data JPA / Hibernate
- **Databases**:
  - **H2 Database** (In-Memory for zero-setup local dev and unit testing)
  - **PostgreSQL** (Driver included + Spring profile configured for staging/production)
- **Validation**: Jakarta Bean Validation (`jakarta.validation`)
- **Frontend**: HTML5, CSS3, Vanilla JavaScript (served directly by Spring Boot)
- **Testing**: JUnit 5, Mockito, Spring Boot `MockMvc`
- **JSON Serialization**: Jackson

---

## Project Architecture & File Details

```text
project-management/
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── example/
    │   │           └── projectmanagement/
    │   │               ├── ProjectManagementApplication.java
    │   │               ├── controller/
    │   │               │   └── ProjectController.java
    │   │               ├── dto/
    │   │               │   ├── ProjectRequest.java
    │   │               │   └── ProjectResponse.java
    │   │               ├── exception/
    │   │               │   ├── ErrorDetails.java
    │   │               │   ├── GlobalExceptionHandler.java
    │   │               │   └── ResourceNotFoundException.java
    │   │               ├── model/
    │   │               │   ├── Project.java
    │   │               │   └── ProjectStatus.java
    │   │               ├── repository/
    │   │               │   └── ProjectRepository.java
    │   │               └── service/
    │   │                   ├── ProjectService.java
    │   │                   └── ProjectServiceImpl.java
    │   └── resources/
    │       ├── application.properties
    │       ├── application-postgres.properties
    │       └── static/
    │           ├── index.html
    │           ├── css/
    │           │   └── styles.css
    │           └── js/
    │               └── app.js
    └── test/
        └── java/
            └── com/
                └── example/
                    └── projectmanagement/
                        ├── controller/
                        │   └── ProjectControllerTest.java
                        └── service/
                            └── ProjectServiceTest.java
```

### Key Source Files Explained

| File Path | Description / Role |
| :--- | :--- |
| `pom.xml` | Complete Maven build file declaring Spring Boot starter dependencies, H2, PostgreSQL, and plugins. |
| `ProjectManagementApplication.java` | Spring Boot main entry point annotated with `@SpringBootApplication`. |
| `model/Project.java` | JPA Entity representing the `projects` database table. |
| `model/ProjectStatus.java` | Enum defining project status (`PLANNED`, `IN_PROGRESS`, `COMPLETED`, `ON_HOLD`). |
| `dto/ProjectRequest.java` | Request DTO containing Jakarta Validation constraints for creating/updating projects. |
| `dto/ProjectResponse.java` | Response DTO mapping entity data for clean JSON serialization. |
| `repository/ProjectRepository.java` | Spring Data JPA Repository interface extending `JpaRepository<Project, Long>`. |
| `service/ProjectService.java` | Interface defining business logic operations for Project management. |
| `service/ProjectServiceImpl.java` | Implementation of `ProjectService` handling transactions, validations, and persistence. |
| `controller/ProjectController.java` | REST Controller exposing HTTP endpoints under `/api/projects`. |
| `exception/ResourceNotFoundException.java` | Custom exception thrown when a project is not found by ID (HTTP 404). |
| `exception/ErrorDetails.java` | Standardized error payload DTO for returning clean error responses. |
| `exception/GlobalExceptionHandler.java` | `@RestControllerAdvice` handling exceptions, validation failures, and HTTP status mapping. |
| `resources/application.properties` | Primary configuration file specifying H2 in-memory settings and JPA options. |
| `resources/application-postgres.properties` | PostgreSQL profile configuration for production/container environments. |
| `resources/static/index.html` | Dashboard HTML page served automatically at `http://localhost:8080/`. |
| `resources/static/css/styles.css` | Modern CSS stylesheet with responsive design, cards, and modal styling. |
| `resources/static/js/app.js` | Vanilla JS file handling REST API requests (`fetch`), DOM updates, and modal logic. |
| `test/.../ProjectServiceTest.java` | Mockito unit tests verifying business logic in isolation. |
| `test/.../ProjectControllerTest.java` | `MockMvc` integration tests verifying REST endpoints, status codes, and validation logic. |

---

## Prerequisites

Ensure you have the following installed on your system:

- **Java Development Kit (JDK)**: Version 17 or 21 (Java 21 recommended)
- **Apache Maven**: Version 3.8 or higher

Verify installation:
```bash
java -version
mvn -version
```

---

## Maven Build & Run Commands

The project strictly supports all standard Maven lifecycle commands:

### 1. Clean Build Directory
Deletes the `target/` build directory:
```bash
mvn clean
```

### 2. Run Test Suite
Executes all unit and integration tests (`ProjectServiceTest`, `ProjectControllerTest`):
```bash
mvn test
```

### 3. Package Application into Executable JAR
Compiles code, runs tests, and packages the application into `target/project-management-1.0.0.jar`:
```bash
mvn clean package
```

### 4. Run Application via Maven Plugin
Starts the Spring Boot application locally on port 8080:
```bash
mvn spring-boot:run
```

### 5. Run Application via Executable JAR
```bash
java -jar target/project-management-1.0.0.jar
```

Once running, access the web application at:
👉 **[http://localhost:8080](http://localhost:8080)**

---

## Domain Model & Validation

### Project Entity Fields

| Field Name | Type | Validation Rules | Description |
| :--- | :--- | :--- | :--- |
| `id` | `Long` | Auto-generated (`IDENTITY`) | Primary Key |
| `name` | `String` | `@NotBlank`, Max 100 chars | Project title/name |
| `description` | `String` | Max 1000 chars | Detailed project description |
| `owner` | `String` | `@NotBlank`, Max 100 chars | Project lead/owner name |
| `status` | `ProjectStatus` | `@NotNull` | Enum: `PLANNED`, `IN_PROGRESS`, `COMPLETED`, `ON_HOLD` |
| `startDate` | `LocalDate` | Format: `YYYY-MM-DD` | Project start date |
| `endDate` | `LocalDate` | Format: `YYYY-MM-DD` | Project end date |

---

## REST API Documentation

Base Endpoint: `http://localhost:8080/api/projects`

### Summary Table

| HTTP Method | Endpoint | Description | Success Status | Error Statuses |
| :--- | :--- | :--- | :--- | :--- |
| `GET` | `/api/projects` | Fetch all projects | `200 OK` | `500 Internal Error` |
| `GET` | `/api/projects/{id}` | Fetch project by ID | `200 OK` | `404 Not Found` |
| `POST` | `/api/projects` | Create a new project | `201 Created` | `400 Bad Request` |
| `PUT` | `/api/projects/{id}` | Update existing project | `200 OK` | `400 Bad Request`, `404 Not Found` |
| `DELETE` | `/api/projects/{id}` | Delete project by ID | `204 No Content` | `404 Not Found` |

---

### Detailed Endpoint Specifications & cURL Examples

#### 1. Get All Projects
- **URL**: `GET /api/projects`
- **cURL Command**:
  ```bash
  curl -X GET http://localhost:8080/api/projects
  ```
- **Response (`200 OK`)**:
  ```json
  [
    {
      "id": 1,
      "name": "Microservices Migration",
      "description": "Decompose monolith into Spring Boot microservices",
      "owner": "Alice Johnson",
      "status": "IN_PROGRESS",
      "startDate": "2026-01-15",
      "endDate": "2026-07-30"
    }
  ]
  ```

#### 2. Get Project by ID
- **URL**: `GET /api/projects/{id}`
- **cURL Command**:
  ```bash
  curl -X GET http://localhost:8080/api/projects/1
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "id": 1,
    "name": "Microservices Migration",
    "description": "Decompose monolith into Spring Boot microservices",
    "owner": "Alice Johnson",
    "status": "IN_PROGRESS",
    "startDate": "2026-01-15",
    "endDate": "2026-07-30"
  }
  ```

#### 3. Create a Project
- **URL**: `POST /api/projects`
- **Header**: `Content-Type: application/json`
- **cURL Command**:
  ```bash
  curl -X POST http://localhost:8080/api/projects \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Kubernetes Cluster Deployment",
      "description": "Setup EKS cluster with Helm charts",
      "owner": "DevOps Team",
      "status": "PLANNED",
      "startDate": "2026-09-01",
      "endDate": "2026-11-15"
    }'
  ```
- **Response (`201 Created`)**:
  ```json
  {
    "id": 2,
    "name": "Kubernetes Cluster Deployment",
    "description": "Setup EKS cluster with Helm charts",
    "owner": "DevOps Team",
    "status": "PLANNED",
    "startDate": "2026-09-01",
    "endDate": "2026-11-15"
  }
  ```

#### 4. Update an Existing Project
- **URL**: `PUT /api/projects/{id}`
- **Header**: `Content-Type: application/json`
- **cURL Command**:
  ```bash
  curl -X PUT http://localhost:8080/api/projects/2 \
    -H "Content-Type: application/json" \
    -d '{
      "name": "Kubernetes Cluster Deployment",
      "description": "Setup EKS cluster with Helm charts",
      "owner": "DevOps Team",
      "status": "IN_PROGRESS",
      "startDate": "2026-09-01",
      "endDate": "2026-12-01"
    }'
  ```
- **Response (`200 OK`)**:
  ```json
  {
    "id": 2,
    "name": "Kubernetes Cluster Deployment",
    "description": "Setup EKS cluster with Helm charts",
    "owner": "DevOps Team",
    "status": "IN_PROGRESS",
    "startDate": "2026-09-01",
    "endDate": "2026-12-01"
  }
  ```

#### 5. Delete a Project
- **URL**: `DELETE /api/projects/{id}`
- **cURL Command**:
  ```bash
  curl -X DELETE http://localhost:8080/api/projects/2
  ```
- **Response**: `204 No Content`

---

## Error Handling & Validation Responses

The application utilizes `@RestControllerAdvice` in `GlobalExceptionHandler.java` to capture errors and return standardized JSON objects.

### Resource Not Found Error (`404 Not Found`)
```json
{
  "timestamp": "2026-08-21T14:52:00",
  "status": 404,
  "error": "Not Found",
  "message": "Project not found with id: 99",
  "path": "/api/projects/99"
}
```

### Validation Error (`400 Bad Request`)
If required fields are missing or invalid:
```json
{
  "timestamp": "2026-08-21T14:52:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed for request fields",
  "path": "/api/projects",
  "validationErrors": {
    "name": "Project name is required",
    "owner": "Owner is required",
    "status": "Status is required"
  }
}
```

---

## Database Configuration (H2 & PostgreSQL)

### 1. H2 In-Memory Database (Default)
By default, the application runs using an H2 in-memory database configured in `application.properties`. This allows immediate local execution without installing database software.

- **Web Console Enabled**: `http://localhost:8080/h2-console`
- **JDBC URL**: `jdbc:h2:mem:projectdb`
- **Username**: `sa`
- **Password**: *(empty)*

### 2. PostgreSQL Profile (`postgres`)
For testing with PostgreSQL or deploying to Kubernetes/Docker environments, a separate profile file `application-postgres.properties` is provided.

To run using PostgreSQL:

#### Via Maven Command:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

#### Via Environment Variables:
```bash
export SPRING_PROFILES_ACTIVE=postgres
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/projectdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=secretpassword

java -jar target/project-management-1.0.0.jar
```

---

## Frontend Web Dashboard

The application includes a clean single-page web dashboard located in `src/main/resources/static/`.

### Features
- **Project Overview Cards**: Displays live counters for Total, Planned, In Progress, and Completed projects.
- **Project Table**: Lists projects with custom status badges (`PLANNED`, `IN_PROGRESS`, `COMPLETED`, `ON_HOLD`).
- **Add / Edit Modal**: Interactive form for adding or updating projects.
- **Delete Confirmation**: Prompts user before deleting a project.
- **Toast Notifications**: Provides instant user feedback on successful or failed operations.

Access at: `http://localhost:8080/`

---

## Testing Suite

The project features a full unit and integration test suite:

1. **`ProjectServiceTest`**: Tests business logic, entity mapping, and repository interaction using Mockito.
2. **`ProjectControllerTest`**: Tests REST endpoint contracts, HTTP status codes, JSON response serialization, and Bean Validation rules using `MockMvc`.

Run all tests:
```bash
mvn test
```

---

## CI/CD Readiness & Future Artifacts

This Java/Spring Boot/Maven application is intentionally kept clean of deployment manifests so you can practice building CI/CD pipelines yourself.

You can add the following files to this project in your CI/CD practice workflow:
- **`Dockerfile`**: To containerize the application target JAR (`target/project-management-1.0.0.jar`).
- **`Jenkinsfile`**: To automate build, test, and containerization steps in Jenkins pipelines.
- **`Kubernetes YAMLs`**: `deployment.yaml`, `service.yaml`, and `ingress.yaml` to deploy to local or cloud Kubernetes clusters (minikube, k3s, GKE).
- **`Docker Compose`**: To orchestrate the application together with a PostgreSQL container for local integration testing.
