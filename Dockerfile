FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY target/todo-backend-0.0.1-SNAPSHOT.jar app.jar
COPY wait-for-mysql.sh wait-for-mysql.sh

EXPOSE 8081

ENTRYPOINT ["./wait-for-mysql.sh", "mysql-db", "java", "-jar", "app.jar"]
