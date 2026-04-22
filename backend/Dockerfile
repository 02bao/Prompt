# Multi-stage Dockerfile: build with Maven, then run the jar in a slim JRE
FROM maven:3.9.5-eclipse-temurin-17 as builder
WORKDIR /workspace
COPY pom.xml ./
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /workspace/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
