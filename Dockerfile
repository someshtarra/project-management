FROM maven:3.9-eclipse-temurin-21-alpine AS builder

WORKDIR /app

COPY . .

RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Upgrade Alpine OS packages to ensure zero vulnerabilities for Trivy scan
RUN apk upgrade --no-cache

RUN addgroup -S somesh && adduser -S somesh -G somesh

COPY --from=builder /app/target/project-management-1.0.0.jar app.jar

RUN chown -R somesh:somesh /app

USER somesh

EXPOSE 8080

ENV SPRING_PROFILES_ACTIVE=default
ENV JAVA_OPTS="-Xms256m -Xmx512m"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
