# 🏋️ Fitness Tracker Application

A production-grade **RESTful Fitness Tracking Application** built with **Spring Boot 4**, **Spring Security**, **MySQL**, and a built-in static HTML/JS frontend. 

The application allows users to register, authenticate via JWT, log various fitness activities, interact with a community of athletes, and receive personalized recommendations—all through a secure, well-documented API.

---

## 📖 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture Overview](#-architecture-overview)
- [Project Structure](#-project-structure)
- [Database Schema](#-database-schema)
- [API Endpoints](#-api-endpoints)
- [Authentication & Authorization](#-authentication--authorization)
- [Error Handling](#-error-handling)
- [Setup & Installation](#-setup--installation)
- [Deployment](#-deployment)
- [Usage Examples](#-usage-examples)
- [API Documentation (Swagger)](#-api-documentation-swagger)
- [Contact](#-contact)

---

## ✨ Features

- **User Registration & Login** — Secure signup with email validation and BCrypt password hashing.
- **JWT Authentication** — Stateless token-based auth with role claims (USER / ADMIN).
- **Activity Tracking** — Log fitness activities with type, duration, calories burned, start time, and custom metrics.
- **10 Activity Types** — WALKING, RUNNING, CYCLING, SWIMMING, WEIGHT_TRAINING, YOGA, CARDIO, STRETCHING, HIIT, OTHERS.
- **Recommendations Engine** — Generate and retrieve fitness recommendations with improvements, suggestions, and safety tips per activity.
- **Community Features** — Browse other athletes and send them personalized tips.
- **Built-in Responsive Frontend** — A modern, dark-themed responsive UI included directly in the Spring Boot application (HTML/CSS/JS).
- **Role-Based Access Control** — Separate `USER` and `ADMIN` roles with endpoint-level authorization.
- **Input Validation** — Request body validation using Jakarta Bean Validation (`@NotBlank`, `@Email`).
- **Global Exception Handling** — Centralized validation error responses via `@RestControllerAdvice`.
- **Swagger / OpenAPI 3 Documentation** — Interactive API docs out of the box.
- **JSON Column Support** — Flexible `additionalMetrics`, `improvements`, `suggestions`, and `safety` stored as native JSON in MySQL.

---

## 🛠 Tech Stack

| Layer              | Technology                                      |
|--------------------|--------------------------------------------------|
| **Language**       | Java 21 / JavaScript                             |
| **Backend**        | Spring Boot 4.0.2                                |
| **Security**       | Spring Security + JWT (jjwt 0.13.0)              |
| **Database**       | MySQL 8+                                         |
| **ORM**            | Spring Data JPA / Hibernate                      |
| **Frontend**       | Vanilla HTML5, CSS3, JavaScript                  |
| **Validation**     | Jakarta Bean Validation                          |
| **API Docs**       | SpringDoc OpenAPI 3.0.2 (Swagger UI)             |
| **Build Tool**     | Maven                                            |

---

## 🏗 Architecture Overview

The application follows a **layered architecture** pattern:

```text
Client Request
      │
      ▼
┌─────────────────────┐
│  JWT Auth Filter     │  ← Extracts & validates JWT, sets SecurityContext
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  Controller Layer    │  ← REST endpoints, request/response mapping
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  Service Layer       │  ← Business logic, DTO ↔ Entity mapping
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  Repository Layer    │  ← JPA interfaces, database queries
└─────────┬───────────┘
          ▼
┌─────────────────────┐
│  MySQL Database      │  ← Persistent storage
└─────────────────────┘
```

---

## 📁 Project Structure

```text
FitnessTacker/
├── pom.xml                          # Maven project configuration & dependencies
├── Dockerfile                       # Docker image definition
├── docker-compose.yml               # Multi-container orchestration (App + MySQL)
├── src/
│   ├── main/
│   │   ├── java/com/example/FitnessTacker/
│   │   │   ├── FitnessTackerApplication.java    # Spring Boot entry point
│   │   │   ├── Model/                           # JPA Entities & Enums
│   │   │   ├── controller/                      # REST Controllers
│   │   │   ├── service/                         # Business Logic
│   │   │   ├── repository/                      # Data Access (Spring Data JPA)
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   ├── Security/                        # Security Configuration
│   │   │   ├── config/                          # App Configuration
│   │   │   └── exception/                       # Exception Handling
│   │   │
│   │   └── resources/
│   │       ├── application.properties           # Database & App config
│   │       └── static/                          # Frontend UI (HTML/CSS/JS)
```

---

## 🗄 Database Schema

The application uses **3 main tables** with UUID-based primary keys, managed by Hibernate.

### Entity Relationship

- **User (1) → (N) Activity**: One user can log multiple activities.
- **User (1) → (N) Recommendations**: One user can receive multiple recommendations.
- **Activity (1) → (N) Recommendations**: One activity can have multiple recommendations.

---

## 🚀 Setup & Installation

Follow these steps to run the project locally.

### Prerequisites
- **Java 21** installed
- **Maven** installed
- **MySQL 8+** running locally or via Docker

### 1. Database Configuration
Create a database in your local MySQL instance:
```sql
CREATE DATABASE fitness_demo;
```

Update your `src/main/resources/application.properties` with your database credentials (ensure you do not commit sensitive passwords):
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/fitness_demo
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

### 2. Build the Application
Navigate to the project root and run:
```bash
./mvnw clean install
```

### 3. Run the Application
Start the Spring Boot server:
```bash
./mvnw spring-boot:run
```
The application will start on `http://localhost:8080`. The embedded frontend can be accessed immediately at that URL.

---

## 🔌 API Endpoints

### 🔐 Authentication — `/api/auth`
| Method | Endpoint             | Auth Required | Description                     |
|--------|----------------------|---------------|---------------------------------|
| POST   | `/api/auth/register` | ❌ No          | Register a new user             |
| POST   | `/api/auth/login`    | ❌ No          | Login and receive a JWT token   |

### 🏃 Activities — `/api/activities`
| Method | Endpoint           | Auth Required | Description                          |
|--------|--------------------|---------------|--------------------------------------|
| POST   | `/api/activities`  | ✅ Yes (JWT)   | Log a new fitness activity           |
| GET    | `/api/activities`  | ✅ Yes (JWT)   | Get all activities for a user        |

### 👥 Users — `/api/users`
| Method | Endpoint       | Auth Required | Description              |
|--------|----------------|---------------|--------------------------|
| GET    | `/api/users`   | ❌ No (Public) | List all registered users |

### 💡 Recommendations — `/api/recommendation`
| Method | Endpoint                                  | Auth Required | Description                              |
|--------|-------------------------------------------|---------------|------------------------------------------|
| POST   | `/api/recommendation/generate`            | ✅ Yes (JWT)   | Generate a recommendation for an activity |
| GET    | `/api/recommendation/user/{userId}`       | ✅ Yes (JWT)   | Get all recommendations for a user        |
| GET    | `/api/recommendation/activity/{activityId}` | ✅ Yes (JWT) | Get all recommendations for an activity   |

---

## 🔒 Authentication & Authorization

- **JWT Flow**: Users log in to receive a JWT token. All secured endpoints require the `Authorization: Bearer <token>` header.
- **Passwords**: Hashed securely using **BCrypt**.
- **Roles**: Routes can be protected based on `USER` or `ADMIN` roles.

---

## ⚠️ Error Handling

The application uses a **Global Exception Handler** (`@RestControllerAdvice`).
- Validation errors (e.g., missing fields, bad email) return a `400 Bad Request` with field-specific messages.
- Authentication errors return a `401 Unauthorized`.

---

## 🚢 Deployment

### Using Docker Compose
A `docker-compose.yml` is provided to run both the application and MySQL 8 together effortlessly.

1. Ensure Docker is running.
2. In the project root, start the containers:
   ```bash
   docker-compose up -d
   ```
3. The app will be available on `http://localhost:8085` (as configured in the compose file).

**Environment Variables (docker-compose.yml):**
Update the docker-compose file with secure credentials before deploying to a production environment. Do not use default or easily guessable passwords.

---

## 📋 Usage Examples

### 1. Register a User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "athlete@example.com",
    "password": "SecurePassword123",
    "firstName": "Jane",
    "lastName": "Smith"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "athlete@example.com",
    "password": "SecurePassword123"
  }'
```
*(Copy the `token` from the response for the next steps)*

### 3. Track an Activity
```bash
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -d '{
    "userId": "<USER_ID>",
    "type": "YOGA",
    "duration": 60,
    "caloriesBurned": 200,
    "startTime": "2026-04-03T07:00:00"
  }'
```

---

## 🖥 Frontend UI

The application includes a fully responsive frontend served directly by Spring Boot. No separate frontend setup is required!
Access it simply by navigating to `http://localhost:8080`.

- **Login / Register** (`index.html`)
- **Dashboard** (`dashboard.html`)
- **Activities** (`activities.html`)
- **Tips Inbox** (`recommendations.html`)
- **Community** (`community.html`)

---

## 📚 API Documentation (Swagger)

Interactive API documentation is provided by SpringDoc OpenAPI.

| Resource          | URL                                         |
|-------------------|---------------------------------------------|
| Swagger UI        | `http://localhost:8080/swagger-ui.html`     |
| OpenAPI JSON      | `http://localhost:8080/v3/api-docs`         |

---

## 📬 Contact

- **Author:** Sudhanshu Chauhan
- **Project Link:** (Add your repository link here)

---

<div align="center">
<b>⭐ If you found this project helpful, please give it a star! ⭐</b>
</div>
