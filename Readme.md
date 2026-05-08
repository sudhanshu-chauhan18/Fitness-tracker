# 🏋️ Fitness Tracker API

A production-grade **RESTful Fitness Tracking API** built with **Spring Boot 4**,
**Spring Security**, and **MySQL**. The application allows users to register, authenticate 
via JWT, log fitness activities (running, cycling, yoga, etc.), and receive personalized 
recommendations — all through a secure, well-documented API.

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
- [Usage Examples](#-usage-examples)
- [API Documentation (Swagger)](#-api-documentation-swagger)
- [Contact](#-contact)

---

## ✨ Features

- **User Registration & Login** — Secure signup with email validation and BCrypt password hashing
- **JWT Authentication** — Stateless token-based auth with role claims (USER / ADMIN)
- **Activity Tracking** — Log fitness activities with type, duration, calories burned, start time, and custom metrics
- **10 Activity Types** — WALKING, RUNNING, CYCLING, SWIMMING, WEIGHT_TRAINING, YOGA, CARDIO, STRETCHING, HIIT, OTHERS
- **Recommendations Engine** — Generate and retrieve fitness recommendations with improvements, suggestions, and safety tips per activity
- **Role-Based Access Control** — Separate `USER` and `ADMIN` roles with endpoint-level authorization
- **Input Validation** — Request body validation using Jakarta Bean Validation (`@NotBlank`, `@Email`)
- **Global Exception Handling** — Centralized validation error responses via `@RestControllerAdvice`
- **Swagger / OpenAPI 3 Documentation** — Interactive API docs out of the box
- **JSON Column Support** — Flexible `additionalMetrics`, `improvements`, `suggestions`, and `safety` stored as native JSON in MySQL
- **Audit Timestamps** — Automatic `createdAt` / `updatedAt` on all entities via Hibernate

---

## 🛠 Tech Stack

| Layer              | Technology                                      |
|--------------------|--------------------------------------------------|
| **Language**       | Java 21                                          |
| **Framework**      | Spring Boot 4.0.2                                |
| **Security**       | Spring Security + JWT (jjwt 0.13.0)              |
| **Database**       | MySQL 8+                                         |
| **ORM**            | Spring Data JPA / Hibernate                      |
| **Validation**     | Jakarta Bean Validation (spring-boot-starter-validation) |
| **API Docs**       | SpringDoc OpenAPI 3.0.2 (Swagger UI)             |
| **Build Tool**     | Maven (with Maven Wrapper)                       |
| **Serialization**  | Jackson (jackson-databind)                       |
| **Utilities**      | Lombok                                           |

---

## 🏗 Architecture Overview

The application follows a **layered architecture** pattern:

```
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

```
FitnessTacker/
├── pom.xml                          # Maven project configuration & dependencies
├── mvnw / mvnw.cmd                  # Maven Wrapper scripts
├── Dockerfile                       # Docker image definition (Eclipse Temurin JRE 21)
├── docker-compose.yml               # Multi-container orchestration (App + MySQL)
├── src/
│   ├── main/
│   │   ├── java/com/example/FitnessTacker/
│   │   │   ├── FitnessTackerApplication.java    # Spring Boot entry point
│   │   │   │
│   │   │   ├── Model/                           # JPA Entities & Enums
│   │   │   │   ├── User.java                    # User entity (UUID PK, email, roles)
│   │   │   │   ├── Activity.java                # Activity entity (type, duration, calories, JSON metrics)
│   │   │   │   ├── Recommendations.java         # Recommendations entity (improvements, suggestions, safety)
│   │   │   │   ├── ActivityType.java            # Enum: 10 supported activity types
│   │   │   │   └── UserRole.java                # Enum: USER, ADMIN
│   │   │   │
│   │   │   ├── controller/                      # REST Controllers
│   │   │   │   ├── AuthController.java          # Register & Login endpoints
│   │   │   │   ├── ActivityController.java      # Activity CRUD endpoints
│   │   │   │   ├── UserController.java          # User listing endpoint
│   │   │   │   └── RecommendationController.java # Recommendation endpoints
│   │   │   │
│   │   │   ├── service/                         # Business Logic
│   │   │   │   ├── UserService.java             # Registration, authentication, mapping
│   │   │   │   ├── ActivityService.java         # Activity tracking & retrieval
│   │   │   │   └── RecommendationService.java   # Recommendation generation & retrieval
│   │   │   │
│   │   │   ├── repository/                      # Data Access (Spring Data JPA)
│   │   │   │   ├── UserRepository.java          # findByEmail
│   │   │   │   ├── ActivityRepository.java      # findByUserId
│   │   │   │   └── RecommendationRepository.java # findByUserId, findByActivityId
│   │   │   │
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── RegisterRequest.java         # Registration input (validated)
│   │   │   │   ├── LoginRequest.java            # Login input (email + password)
│   │   │   │   ├── LoginResponse.java           # JWT token + user details
│   │   │   │   ├── UserResponse.java            # User output DTO
│   │   │   │   ├── ActivityRequest.java         # Activity input DTO
│   │   │   │   ├── ActivityResponse.java        # Activity output DTO
│   │   │   │   └── RecommendationRequest.java   # Recommendation input DTO
│   │   │   │
│   │   │   ├── Security/                        # Security Configuration
│   │   │   │   ├── SecurityConfig.java          # Filter chain, CSRF, endpoint rules
│   │   │   │   ├── JWTUtils.java                # Token generation, validation, parsing
│   │   │   │   ├── JwtAuthenticationFilter.java # OncePerRequestFilter for JWT
│   │   │   │   └── CustomUserDetailsService.java # UserDetailsService implementation
│   │   │   │
│   │   │   ├── config/                          # App Configuration
│   │   │   │   ├── OpenAPIConfig.java           # Swagger/OpenAPI metadata
│   │   │   │   └── CorsConfig.java              # CORS policy for /api/** routes
│   │   │   │
│   │   │   └── exception/                       # Exception Handling
│   │   │       └── GlobalExceptionHandler.java  # Validation error handler
│   │   │
│   │   └── resources/
│   │       ├── application.properties           # Database & Hibernate config
│   │       └── static/                          # Frontend UI (served by Spring)
│   │           ├── index.html                   # Login / Register page
│   │           ├── dashboard.html               # User dashboard with stats
│   │           ├── activities.html              # Activity logging & history
│   │           ├── recommendations.html         # Tips inbox viewer
│   │           ├── community.html               # Browse athletes & give tips
│   │           ├── styles.css                   # Global dark-theme stylesheet
│   │           └── app.js                       # Shared API layer & UI utilities
│   │
│   └── test/                                    # Test directory (scaffold only)
└── target/                                      # Compiled output
```

---

## 🗄 Database Schema

The application uses **3 main tables** with UUID-based primary keys, managed by Hibernate with `ddl-auto=update`.

### Entity Relationship Diagram

```
┌──────────────┐       ┌──────────────────┐       ┌─────────────────────┐
│     User     │       │     Activity     │       │   Recommendations   │
├──────────────┤       ├──────────────────┤       ├─────────────────────┤
│ id (UUID) PK │──┐    │ id (UUID) PK     │──┐    │ id (UUID) PK        │
│ email (UQ)   │  │    │ user_id FK       │  │    │ user_id FK          │
│ password     │  ├───▶│ type (ENUM)      │  ├───▶│ activity_id FK      │
│ firstName    │  │    │ duration         │  │    │ type                │
│ lastName     │  │    │ caloriesBurned   │  │    │ recommendation      │
│ role (ENUM)  │  │    │ startTime        │  │    │ improvements (JSON) │
│ createdAt    │  │    │ additionalMetrics│  │    │ suggestions  (JSON) │
│ updatedAt    │  │    │   (JSON)         │  │    │ safety       (JSON) │
└──────────────┘  │    │ createdAt        │  │    │ createdAt           │
                  │    │ updatedAt        │  │    │ updatedAt           │
                  │    └──────────────────┘  │    └─────────────────────┘
                  │                          │
                  │    1:N (User → Activity)  │    1:N (User → Recommendations)
                  │                          │    1:N (Activity → Recommendations)
                  └──────────────────────────┘
```

### Relationships

| Relationship                 | Type         | Cascade        | Details                           |
|------------------------------|--------------|----------------|-----------------------------------|
| User → Activity              | One-to-Many  | ALL + Orphan   | `mappedBy = "user"`               |
| User → Recommendations      | One-to-Many  | ALL + Orphan   | `mappedBy = "user"`               |
| Activity → Recommendations  | One-to-Many  | ALL + Orphan   | `mappedBy = "activity"`           |

### Enums

**`ActivityType`**:
`WALKING` · `RUNNING` · `CYCLING` · `SWIMMING` · `WEIGHT_TRAINING` · `YOGA` · `CARDIO` · `STRETCHING` · `HIIT` · `OTHERS`

**`UserRole`**:
`USER` · `ADMIN`

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
| GET    | `/api/activities`  | ✅ Yes (JWT)   | Get all activities for a user (via `X-USER-ID` header) |

### 👥 Users — `/api/users`

| Method | Endpoint       | Auth Required | Description              |
|--------|----------------|---------------|--------------------------|
| GET    | `/api/users`   | ❌ No (Public) | List all registered users |

> **Note:** This endpoint is publicly accessible and used by the Community frontend page to display all athletes.

### 💡 Recommendations — `/api/recommendation`

| Method | Endpoint                                  | Auth Required | Description                              |
|--------|-------------------------------------------|---------------|------------------------------------------|
| POST   | `/api/recommendation/generate`            | ✅ Yes (JWT)   | Generate a recommendation for an activity |
| GET    | `/api/recommendation/user/{userId}`       | ✅ Yes (JWT)   | Get all recommendations for a user        |
| GET    | `/api/recommendation/activity/{activityId}` | ✅ Yes (JWT) | Get all recommendations for an activity   |

---

### Detailed Request / Response Reference

#### `POST /api/auth/register`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePass123",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER"
}
```

**Validation Rules:**
- `email` — Required, must be valid email format
- `password` — Required, cannot be blank
- `firstName`, `lastName`, `role` — Optional (`role` defaults to `USER`)

**Response `200 OK`:**
```json
{
  "id": "a1b2c3d4-...",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "firstName": "John",
  "lastName": "Doe"
}
```

---

#### `POST /api/auth/login`

**Request Body:**
```json
{
  "email": "john@example.com",
  "password": "securePass123"
}
```

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "user": {
    "id": "a1b2c3d4-...",
    "email": "john@example.com",
    "password": "$2a$10$...",
    "firstName": "John",
    "lastName": "Doe"
  }
}
```

**Response `401 Unauthorized`:** Returned on invalid credentials.

---

#### `POST /api/activities`

**Request Body:**
```json
{
  "userId": "a1b2c3d4-...",
  "type": "RUNNING",
  "duration": 45,
  "caloriesBurned": 400,
  "startTime": "2026-04-03T06:30:00",
  "additionalMetrics": {
    "distanceKm": 5.2,
    "avgHeartRate": 145,
    "steps": 6800
  }
}
```

**Response `200 OK`:**
```json
{
  "id": "e5f6g7h8-...",
  "userId": "a1b2c3d4-...",
  "type": "RUNNING",
  "duration": 45,
  "caloriesBurned": 400,
  "startTime": "2026-04-03T06:30:00",
  "additionalMetrics": {
    "distanceKm": 5.2,
    "avgHeartRate": 145,
    "steps": 6800
  },
  "createdAt": "2026-04-03T12:00:00",
  "updatedAt": "2026-04-03T12:00:00"
}
```

---

#### `GET /api/activities`

**Headers:**
```
Authorization: Bearer <jwt_token>
X-USER-ID: a1b2c3d4-...
```

**Response `200 OK`:** Array of `ActivityResponse` objects.

---

#### `POST /api/recommendation/generate`

**Request Body:**
```json
{
  "userId": "a1b2c3d4-...",
  "activityId": "e5f6g7h8-...",
  "improvements": [
    "Increase pace gradually",
    "Add interval training"
  ],
  "suggestions": [
    "Try hill sprints for endurance",
    "Include cool-down stretches"
  ],
  "safety": [
    "Warm up for at least 5 minutes",
    "Stay hydrated during the run"
  ]
}
```

**Response `200 OK`:**
```json
{
  "id": "i9j0k1l2-...",
  "type": null,
  "recommendation": null,
  "improvements": ["Increase pace gradually", "Add interval training"],
  "suggestions": ["Try hill sprints for endurance", "Include cool-down stretches"],
  "safety": ["Warm up for at least 5 minutes", "Stay hydrated during the run"],
  "createdAt": "2026-04-03T12:05:00",
  "updatedAt": "2026-04-03T12:05:00"
}
```

> **Note:** The `type` and `recommendation` text fields are present in the entity schema but are **not populated** by the current `RecommendationService` implementation. They can be used for future enhancements (e.g., AI-generated recommendation text).

---

#### `GET /api/recommendation/user/{userId}`

**Response `200 OK`:** Array of `Recommendations` objects for the given user.

---

#### `GET /api/recommendation/activity/{activityId}`

**Response `200 OK`:** Array of `Recommendations` objects for the given activity.

---

## 🔒 Authentication & Authorization

### JWT Flow

```
1. User registers  →  POST /api/auth/register
2. User logs in    →  POST /api/auth/login        →  Receives JWT token
3. User calls API  →  Authorization: Bearer <token> →  Filter validates token
4. Request passes  →  SecurityContext populated     →  Controller processes request
```

### Token Details

| Property       | Value                          |
|----------------|--------------------------------|
| Algorithm      | HMAC-SHA (HS256)               |
| Expiration     | 48 hours (172,800,000 ms)      |
| Subject        | User UUID                      |
| Custom Claims  | `roles` (list of role strings) |
| Secret         | Base64-encoded 256-bit key     |

### Endpoint Security Rules

| Pattern              | Access               |
|----------------------|----------------------|
| `/api/auth/**`       | Public (permit all)  |
| `/api/admin/**`      | `ADMIN` role only    |
| `/swagger-ui/**`     | Public (permit all)  |
| `/v3/api-docs/**`    | Public (permit all)  |
| All other endpoints  | Authenticated (JWT)  |

### Password Security

- Passwords are hashed using **BCrypt** via `BCryptPasswordEncoder`
- Raw passwords are never stored in the database
- CSRF protection is disabled (appropriate for stateless REST APIs)

---

## ⚠️ Error Handling

The application implements a **Global Exception Handler** using `@RestControllerAdvice`.

### Handled Exceptions

| Exception                          | HTTP Status | Response Format                      |
|------------------------------------|-------------|--------------------------------------|
| `MethodArgumentNotValidException`  | `400`       | `{ "fieldName": "error message" }`   |
| `AuthenticationException` (login)  | `401`       | Empty body                           |
| `RuntimeException` (entity not found) | `500`    | Default Spring error (unhandled)     |

### Example Validation Error Response

```json
{
  "email": "Email is Required.",
  "password": "Password is Required."
}
```

---
### Key Configuration Notes

| Property                 | Description                                                                 |
|--------------------------|-----------------------------------------------------------------------------|
| `ddl-auto=update`        | Hibernate auto-creates/updates schema. Use `validate` in production.       |
| `show-sql=true`          | Logs all SQL statements. Disable in production for performance.            |
| JWT Secret               | Stored in `JWTUtils.java`. **Externalize to env vars for production.**     |
| JWT Expiration           | Set to 48 hours (172800000 ms) in `JWTUtils.java`.                        |

### CORS Configuration

The application includes a global CORS policy via `CorsConfig.java`:

| Setting            | Value                                 |
|--------------------|---------------------------------------|
| **Allowed Origins**| `*` (all origins)                     |
| **Allowed Methods**| `GET`, `POST`, `PUT`, `DELETE`, `OPTIONS` |
| **Allowed Headers**| `*` (all headers)                     |
| **Path Pattern**   | `/api/**`                             |

> **⚠️ Production Note:** Restrict `allowedOrigins` to your actual frontend domain(s) before deploying to production.


---

## 📋 Usage Examples

### Full Workflow with cURL

#### 1. Register a User

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "athlete@example.com",
    "password": "MySecure@123",
    "firstName": "Jane",
    "lastName": "Smith",
    "role": "USER"
  }'
```

#### 2. Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "athlete@example.com",
    "password": "MySecure@123"
  }'
```

> Copy the `token` from the response for subsequent requests.

#### 3. Track an Activity

```bash
curl -X POST http://localhost:8080/api/activities \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -d '{
    "userId": "<USER_ID_FROM_REGISTER>",
    "type": "YOGA",
    "duration": 60,
    "caloriesBurned": 200,
    "startTime": "2026-04-03T07:00:00",
    "additionalMetrics": {
      "flexibility_score": 8.5,
      "poses_completed": 15
    }
  }'
```

#### 4. Get User Activities

```bash
curl -X GET http://localhost:8080/api/activities \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -H "X-USER-ID: <USER_ID>"
```

#### 5. Generate a Recommendation

```bash
curl -X POST http://localhost:8080/api/recommendation/generate \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>" \
  -d '{
    "userId": "<USER_ID>",
    "activityId": "<ACTIVITY_ID>",
    "improvements": ["Hold poses longer", "Focus on breathing"],
    "suggestions": ["Try advanced poses", "Add meditation"],
    "safety": ["Avoid over-stretching", "Use a yoga mat"]
  }'
```

#### 6. Get User Recommendations

```bash
curl -X GET http://localhost:8080/api/recommendation/user/<USER_ID> \
  -H "Authorization: Bearer <YOUR_JWT_TOKEN>"
```

---

## 🖥 Frontend UI

The application ships with a **built-in static frontend** served directly by Spring Boot from `src/main/resources/static/`. No separate frontend server is needed.

### Pages

| Page                     | File                    | Description                                              |
|--------------------------|-------------------------|----------------------------------------------------------|
| **Login / Register**     | `index.html`            | Auth page with tabbed Login & Register forms             |
| **Dashboard**            | `dashboard.html`        | Overview stats (total activities, calories, duration), recent activities, activity breakdown chart |
| **Activities**           | `activities.html`       | Log new workouts with custom metrics, view/filter activity history |
| **Tips Inbox**           | `recommendations.html`  | View received tips filtered by category (Improvements, Suggestions, Safety) |
| **Community**            | `community.html`        | Browse all registered athletes, view their activities, and send them personalised tips via a modal |

### Frontend Architecture

- **`app.js`** — Shared module containing:
  - `Auth` — LocalStorage-based session management (token + user)
  - `api()` — Core fetch wrapper with JWT injection and error handling
  - `AuthAPI` / `ActivityAPI` / `RecommendationAPI` / `UserAPI` — Endpoint wrappers
  - `UI` — Toast notifications, button loading states, date/duration formatting, navbar renderer
  - `ACTIVITY_TYPES` — Enum mirror matching the Java `ActivityType` enum

- **`styles.css`** — Dark-theme design system with CSS custom properties, responsive grid layouts, card components, and micro-animations

### API Base URL

The frontend connects to the backend via the `API_BASE` constant in `app.js`:

```js
const API_BASE = "http://localhost:8080";  // Change for Docker: http://localhost:8085
```

### Auth Flow (Frontend)

```
1. User visits index.html  →  Login / Register forms
2. On login success        →  JWT + user object stored in localStorage
3. Protected pages         →  Auth.require() redirects to index.html if not logged in
4. API calls               →  Authorization header auto-injected by api() wrapper
5. Logout                  →  localStorage cleared, redirect to index.html
```

---

## 📚 API Documentation (Swagger)

The project integrates **SpringDoc OpenAPI 3** with a custom configuration providing:

- **Title:** Fitness Tracking API
- **Version:** v1.0
- **Description:** Production Grade API

### Access Points

| Resource          | URL                                         |
|-------------------|---------------------------------------------|
| Swagger UI        | `http://localhost:8080/swagger-ui.html`     |
| OpenAPI JSON      | `http://localhost:8080/v3/api-docs`         |

Both endpoints are **publicly accessible** (no JWT required).

---

## 🚢 Deployment

### Build a Production JAR

```bash
./mvnw clean package -DskipTests
```

The JAR will be generated at: `target/FitnessTacker-0.0.1-SNAPSHOT.jar`

### Docker

The project includes a `Dockerfile` using **Eclipse Temurin JRE 21 Alpine**:

```bash
# Build the JAR first
./mvnw clean package -DskipTests

# Build the Docker image
docker build -t fitness:latest .
```

### Docker Compose (App + MySQL)

A `docker-compose.yml` is provided to run both the application and MySQL 8 together:

```bash
docker compose up -d
```

| Service   | Container Name    | Port Mapping   | Description             |
|-----------|-------------------|----------------|-------------------------|
| `mysql`   | `fitness-mysql`   | `3307:3306`    | MySQL 8 with health check |
| `app`     | `fitness-app`     | `8085:8080`    | Spring Boot application  |

**Environment Variables (docker-compose.yml):**

> ⚠️ Replace the placeholder values below with your own secure credentials before deploying.

| Variable                       | Service | Description                |
|--------------------------------|---------|----------------------------|
| `MYSQL_ROOT_PASSWORD`          | mysql   | MySQL root password        |
| `MYSQL_DATABASE`               | mysql   | Database name to create    |
| `SPRING_DATASOURCE_URL`        | app     | JDBC connection string     |
| `SPRING_DATASOURCE_USERNAME`   | app     | Database username          |
| `SPRING_DATASOURCE_PASSWORD`   | app     | Database password          |

The app container waits for MySQL to be healthy before starting (`depends_on` with `service_healthy` condition).

---

## 🔧 Environment Variables

For production deployments, externalize all sensitive configuration:

| Variable                     | Description                             | Default (Dev Only)      |
|------------------------------|-----------------------------------------|-------------------------|
| `SPRING_DATASOURCE_URL`      | JDBC MySQL connection URL               | `jdbc:mysql://localhost:3306/fitness_demo` |
| `SPRING_DATASOURCE_USERNAME` | Database username                       | `root`                  |
| `SPRING_DATASOURCE_PASSWORD` | Database password                       | *(set your own)*        |
| `JWT_SECRET`                 | Base64-encoded 256-bit HMAC key         | *(hardcoded in JWTUtils — externalize for prod)* |
| `JWT_EXPIRATION_MS`          | Token TTL in milliseconds               | `172800000` (48 hours)  |


---

## 🗺 Roadmap / Future Enhancements

- [ ] AI-powered recommendation text generation (populate `type` and `recommendation` fields)
- [ ] Pagination for activity and recommendation lists
- [ ] Password strength validation and rate limiting
- [ ] User profile edit and avatar upload
- [ ] Activity update and delete endpoints
- [ ] Unit and integration test coverage
- [ ] Externalize JWT secret to environment variable / Spring config
- [ ] Restrict CORS origins for production

---

## 📬 Contact

- **Author:** Sudhanshu Chauhan
- **Email:** sudhanshuchauhan6789@gmail.com

---

<div align="center">

**⭐ If you found this project helpful, please give it a star! ⭐**

</div>
