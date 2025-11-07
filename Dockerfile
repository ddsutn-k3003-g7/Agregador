# Importing JDK and copying required files
FROM maven:3.8.6-openjdk-18 AS build

COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:18-jdk

# Copy the JAR from the build stage
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080