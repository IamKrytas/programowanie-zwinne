# ============================================================================
# This is a Dockerfile for running the backend application in a container
# 
# To build: (while being in the repo root directory)
# > docker build -t ptracker-backend -f ./CONTRIB/docker/backend.Dockerfile ./BACKEND
# ============================================================================

# Build container
FROM gradle:8.6-jdk21 AS build
WORKDIR /opt/ptracker-backend
COPY . .
RUN gradle build
RUN mkdir -p /tmp/build
RUN cp $(find build/libs -name "*.jar" | grep -v "plain") /tmp/build/app.jar

# Run container
FROM eclipse-temurin:21-jdk
WORKDIR /opt/ptracker-backend
COPY --from=build /tmp/build/app.jar app.jar
CMD ["java", "-jar", "app.jar"]
