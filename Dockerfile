# Build stage: use Maven image to build the JAR
FROM maven:3.9.3-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# Final image: just JDK to run the app
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy wait-for-mysql script
COPY wait-for-mysql.sh .
RUN chmod +x wait-for-mysql.sh

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "./wait-for-mysql.sh mysql-db -- java -jar app.jar"]
