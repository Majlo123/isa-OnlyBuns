# isa-OnlyBuns
sa-OnlyBuns is the backend service for a social media web application designed for rabbit enthusiasts. Developed as a project for the Internet Software Architectures course, this application allows users to share photos of their pets, follow other users, and interact through likes and comments.

Key Features & Technologies:

RESTful API: Built with Java and the Spring Boot framework to handle all business logic.

User Authentication: Secure registration and login functionality with mechanisms to handle concurrent requests and prevent brute-force attacks.

Post Management: Endpoints for creating, viewing, liking, and commenting on posts, with image uploads and location data handling.

User Interaction: Functionality for following users, ensuring that a user's home feed is populated with content from followed accounts.

Scheduled Tasks: Automated jobs for sending email notifications to inactive users and deleting unactivated accounts.

Real-time Communication: Group chat functionality implemented using WebSocket technology.

Message Queues: Integration with a message queue system for features like advertising posts and displaying external location data for rabbit care services.

Monitoring: Implemented application monitoring using Prometheus and Grafana to track performance metrics.

Caching: Implemented caching strategies for photos and location data to improve performance.
