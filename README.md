# EduShare Microservices Project

## Overview

EduShare is a simple microservices-based application designed for file sharing between students and professors. This project is a basic implementation of a microservices architecture, utilizing various technologies to provide a scalable and modular solution.

## Technologies Used

- **Docker Compose**: For container orchestration.
- **Kafka**: For handling notifications.
- **Maildev**: For email services during development.
- **Zipkin**: For distributed tracing.
- **API Gateway**: To route requests to the appropriate microservices.
- **Config Server**: For centralized configuration management.
- **Eureka Discovery Service**: For service registration and discovery.
- **AWS S3 LocalStack**: For local file storage emulation.

## Databases

The project includes the following databases, each running in its own Docker container:

- **PostgreSQL**: For storing user and group data.
- **MongoDB**: For storing files and notifications data.

## Getting Started

1. **Clone the Repository**:
   ```bash
   git clone https://github.com/Khalil8451/edushare.git
   cd edushare
   ```

2. **Start Docker Containers**:
   Ensure Docker is installed and running, then start the required services using Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. **Build the Microservices**:
   Navigate to each microservice folder and build the application:
   ```bash
   mvn clean install
   ```

4. **Run Config Server**:
   ```bash
   cd config-server
   mvn spring-boot:run
   ```

5. **Run Discovery Server**:
   ```bash
   cd discovery
   mvn spring-boot:run
   ```

6. **Run Other Services**:
   Run the main microservices in the following order:
   ```bash
   cd user
   mvn spring-boot:run
   ```
   ```bash
   cd file-storage
   mvn spring-boot:run
   ```
   ```bash
   cd notification
   mvn spring-boot:run
   ```

7. **Run API Gateway**:
   ```bash
   cd gateway
   mvn spring-boot:run
   ```

## Testing the Application

Once all services are running, you can test the APIs using Postman or cURL. The API Gateway routes requests to the correct microservices.

## Monitoring and Debugging

- **MailDev UI**: Available at `http://localhost:1080`
- **Zipkin UI**: Available at `http://localhost:9411`
