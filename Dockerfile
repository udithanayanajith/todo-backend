FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy built JAR from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

# Start app directly without database dependency
CMD ["java", "-jar", "app.jar"]