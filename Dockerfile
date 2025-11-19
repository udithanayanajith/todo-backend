FROM eclipse-temurin:17-jdk-alpine AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build the JAR inside Docker
RUN ./mvnw clean package -DskipTests

# Final image
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

COPY wait-for-mysql.sh .
RUN chmod +x wait-for-mysql.sh

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["sh", "-c", "./wait-for-mysql.sh mysql-db -- java -jar app.jar"]
