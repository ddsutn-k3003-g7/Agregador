# Importing JDK and copying required files
FROM maven:3.9.9-eclipse-temurin-17 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

# Copy the JAR from the build stage
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080