# Build stage
FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy Maven files and source code
COPY pom.xml .
COPY src ./src

# If you have Maven wrapper
RUN ./mvnw clean package -DskipTests
# Or if you use local Maven: RUN mvn clean package -DskipTests

# Final image
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy wait-for-mysql script
COPY wait-for-mysql.sh .
RUN chmod +x wait-for-mysql.sh

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["sh", "-c", "./wait-for-mysql.sh mysql-db -- java -jar app.jar"]
