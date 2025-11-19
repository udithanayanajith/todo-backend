Todo Backend API Documentation
==============================

Overview
--------

A Spring Boot Todo application with layered architecture and comprehensive testing.

API Endpoints
-------------

GET

/api/tasks/completed

Get latest 5 completed tasks

GET

/api/tasks/inCompleted

Get latest 5 incomplete tasks

POST

/api/tasks

Create a new task

POST

/api/tasks/{id}/done

Mark a task as completed


Testing
-------

**Test Coverage:**

*   Service Layer Tests (TaskServiceTest)
*   Controller Layer Tests (TaskControllerTest)
*   Repository Layer Tests (TaskRepositoryTest)

Technology Stack
----------------

*   Java + Spring Boot
*   Spring Data JPA
*   H2 Database (Testing)
*   JUnit 5 + Mockito
*   Maven

Run the Application
-------------------

\# Build the project mvn clean install # Run tests mvn test # Run application mvn spring-boot:run
