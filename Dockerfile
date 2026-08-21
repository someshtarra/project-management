# Stage 1: Build stage with Maven and OpenJDK 21
FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

# Copy pom.xml and download dependencies (enables Docker layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy project source code
COPY src ./src

# Package application into executable JAR (skipping tests since tests run in CI pipeline)
RUN mvn clean package -DskipTests

# Stage 2: Runtime stage with lightweight JRE 21
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Create a non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy built JAR artifact from builder stage
COPY --from=builder /app/target/project-management-1.0.0.jar app.jar

# Change ownership to non-root user
RUN chown -R appuser:appgroup /app

# Switch to non-root user
USER appuser

# Expose Spring Boot port
EXPOSE 8080

# Environment variables with sensible defaults
ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_OPTS="-Xms256m -Xmx512m"

# Entrypoint to run the Spring Boot executable JAR
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
