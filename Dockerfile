# --- ETAPA 1: El Constructor (Builder) ---
# Compila, prueba, y genera JAR, reportes y javadoc
FROM gradle:9.1.0-jdk25-alpine AS builder
WORKDIR /app

# Copiamos solo los archivos de build para cachear dependencias
COPY build.gradle.kts settings.gradle.kts ./
COPY gradle ./gradle

# Copiamos el resto del código fuente
COPY src ./src

# 'build' ejecuta 'test' (informe JUnit)
# 'jacocoTestReport' genera el informe de cobertura
# 'javadoc' genera la documentación
# '-x jacocoTestCoverageVerification' excluye la verificación de umbrales de cobertura
RUN gradle build javadoc jacocoTestReport -x jacocoTestCoverageVerification --no-daemon

# --- ETAPA 2: El Contenedor de Artefactos ---
# Esta etapa expone los reportes y docs usando Alpine (tiene shell para copiar)
FROM alpine:latest AS artifacts
WORKDIR /artifacts

# Copiamos los reportes de JUnit
COPY --from=builder /app/build/reports/tests/test /artifacts/test-reports
# Copiamos la documentación Javadoc
COPY --from=builder /app/build/docs/javadoc /artifacts/doc-reports
# (Opcional) Copiamos el informe de JaCoCo
COPY --from=builder /app/build/reports/jacoco /artifacts/coverage-reports


# --- ETAPA 3: La Imagen Final (Runtime) ---
# Imagen ligera solo con el JRE y el JAR
FROM eclipse-temurin:25-jre-alpine AS runtime
WORKDIR /app

# Copiamos el JAR ejecutable desde el 'builder'
COPY --from=builder /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]