# Build stage
FROM gradle:9.1.0-jdk25-alpine AS build
WORKDIR /app
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle
COPY src ./src
RUN gradle clean build -x test --no-daemon

# Artifacts stage for reports
FROM alpine:latest AS artifacts
WORKDIR /artifacts
COPY --from=build /app/build/reports/tests/test /artifacts/test-reports
COPY --from=build /app/build/docs/dokka /artifacts/doc-reports
COPY --from=build /app/build/reports/jacoco /artifacts/coverage-reports

# Runtime stage
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]