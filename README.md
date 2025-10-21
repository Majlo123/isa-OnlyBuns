# isa-OnlyBuns (Backend)

This is the backend service for **OnlyBuns**, a social media web application for rabbit enthusiasts, developed as a project for the Internet Software Architectures course. The application is built using Java and the Spring Boot framework, designed to handle user management, content, real-time interactions, and more.

## Key Features

Based on the project specification, the backend implements the following features:

* **User Management:** Secure user registration with email account activation and login functionality. Includes mechanisms to handle concurrent registration attempts and protection against brute-force attacks by limiting login attempts per IP address.
* **Post Management:** Full CRUD (Create, Read, Update, Delete) functionality for user posts, including image uploads and location data. Implemented logic for handling likes and comments, with transaction management to prevent race conditions.
* **Social Features:** Functionality for following and unfollowing users, with rate limiting to prevent bot activity.
* **Real-time Chat:** Group and individual chat functionality implemented using WebSockets.
* **Scheduled Tasks:** Automated jobs for periodically deleting unactivated accounts and sending email notifications to inactive users.
* **Message Queues:** Integration with a message queue system to communicate with external microservices for advertising posts (fanout) and receiving location data for rabbit care services (direct).
* **Monitoring & Analytics:** Exposes metrics for **Prometheus** to scrape, enabling performance monitoring and activity tracking in **Grafana**. Provides administrative endpoints for application analytics.
* **Caching:** Implemented caching strategies for frequently accessed data such as post images and locations.

## Technology Stack

* **Framework:** Java + Spring Boot
* **Authentication:** JWT (JSON Web Tokens)
* **Database:** PostgreSQL / MySQL (or other relational database)
* **Real-time:** WebSockets
* **Asynchronous Communication:** RabbitMQ / Kafka (Message Queues)
* **Monitoring:** Prometheus, Grafana
* **Caching:** Redis / EhCache

## Setup and Installation

1.  **Prerequisites:**
    * Java JDK (Version 17+)
    * Maven or Gradle
    * A running instance of PostgreSQL (or your chosen database)
    * A running instance of a message broker (e.g., RabbitMQ)

2.  **Configuration:**
    * Update the `application.properties` file with your database credentials, JWT secret, and message queue connection details.

3.  **Build and Run:**
    ```bash
    # Using Maven
    ./mvnw spring-boot:run

    # Using Gradle
    ./gradlew bootRun
    ```

4.  **API Documentation:**
    * The API is documented following the OpenAPI specification and is available at `/swagger-ui.html` once the application is running.
