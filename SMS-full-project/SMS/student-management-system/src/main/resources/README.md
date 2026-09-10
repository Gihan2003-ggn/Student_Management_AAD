# Student Management System (SMS)

A full-stack Student Management System with a Spring Boot REST API backend and a plain HTML/CSS/JS frontend. It manages departments, courses, batches, students, payments, and user authentication with JWT-based security.

## Tech Stack

**Backend**
- Java 21
- Spring Boot 3.3.4
- Spring Data JPA (Hibernate)
- Spring Security + JWT (jjwt 0.12.6)
- MySQL (via `mysql-connector-j`)
- HikariCP (connection pooling)
- Lombok, ModelMapper
- Maven

**Frontend**
- HTML5 / CSS3 / vanilla JavaScript
- Pages: `index.html`, `register.html`, `dashboard.html`, `students.html`, `courses.html`, `departments.html`, `batches.html`, `payments.html`

## Project Structure

```
SMS/
├── student-management-system/          # Spring Boot backend
│   ├── src/main/java/lk/ijse/studentmanagement/
│   │   ├── controller/                 # REST controllers
│   │   ├── service/                    # Business logic
│   │   ├── repository/                 # Spring Data JPA repositories
│   │   ├── entity/                     # JPA entities
│   │   ├── dto/                        # Request/response DTOs
│   │   └── security/                   # JWT filter & security config
│   ├── src/main/resources/
│   │   └── application.properties      # DB, JWT, SMTP config
│   └── pom.xml
└── student-management-system-frontEnd/ # Static frontend
    ├── css/
    ├── js/
    └── *.html
```

## Prerequisites

- JDK 21+
- Maven 3.9+ (or use the included `./mvnw` wrapper)
- MySQL 8+ running locally (or a cloud MySQL instance)
- A modern web browser (for the frontend)

## Setup

### 1. Clone the repository
```bash
git clone <your-repo-url>
cd SMS/student-management-system
```

### 2. Configure the database
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/studentdb?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=Asia/Colombo&allowPublicKeyRetrieval=true
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```
`spring.jpa.hibernate.ddl-auto=update` is set, so tables are created/updated automatically on startup — no manual schema setup needed.

### 3. Configure JWT and mail (optional)
```properties
jwt.secret=<your-own-secret-key>
jwt.expiration=86400000

spring.mail.username=${SMTP_USER:your-email@gmail.com}
spring.mail.password=${SMTP_PASS:your-gmail-app-password}
```

### 4. Build and run
```bash
./mvnw clean install
./mvnw spring-boot:run
```
The API starts on **http://localhost:8080**.

### 5. Run the frontend
Open `student-management-system-frontEnd/index.html` directly in a browser, or serve the folder with any static file server (e.g. the VS Code "Live Server" extension) so it can call the backend API.

## API Overview

Base path: `/api/v1`

| Resource | Endpoint | Methods |
|---|---|---|
| Auth | `/auth/register`, `/auth/login` | POST |
| Health | `/health` | GET |
| Students | `/students`, `/students/{id}` | GET, POST, PUT, DELETE |
| Courses | `/courses`, `/courses/{id}` | GET, POST, PUT, DELETE |
| Departments | `/departments`, `/departments/{id}` | GET, POST, PUT, DELETE |
| Batches | `/batches`, `/batches/{id}` | GET, POST, PUT, DELETE |
| Payments | `/payments`, `/payments/{id}` | GET, POST, DELETE |

Most endpoints require a valid JWT (obtained from `/auth/login`) sent as:
```
Authorization: Bearer <token>
```

## Core Entities

Departments, Courses, Batches, Students, Lecturers, Users, Enrollments (student_courses), Attendance, Assessments, Submissions, Grades, Payments, Payment Items, Timetables, Notifications, Audit Logs.

## Troubleshooting

- **Port 8080 already in use** — stop any previously running instance, or change `server.port` in `application.properties`.
- **`ClassNotFoundException` for a DB driver** — make sure the driver dependency in `pom.xml` matches the `spring.datasource.url` (MySQL vs PostgreSQL), then reload Maven.
- **Cannot connect to DB** — verify MySQL is running and the credentials in `application.properties` are correct.

## License

For academic/coursework use (ITS1114 – Advanced API Development, HDSE 75).

https://documenter.getpostman.com/view/55297882/2sBYAyro1u - my postman api testing