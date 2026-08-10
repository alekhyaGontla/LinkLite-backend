# 🔗 LinkLite - URL Shortener

LinkLite is a full-stack URL shortening application that converts long URLs into short, easy-to-share links. It provides features like custom aliases, URL expiry management, click analytics, and Redis caching for faster redirection.

The backend is developed using **Java Spring Boot** with **PostgreSQL** as the database and **Redis** for caching.

---

# 🚀 Features

## URL Management
- Create short URLs from long URLs
- Generate unique short codes
- Create custom aliases
- Set URL expiration date
- View all shortened URLs
- Delete shortened URLs

## Analytics
- Track total click count
- Track last accessed time
- Monitor URL usage statistics

## Performance
- Redis caching for faster URL retrieval
- Optimized redirect handling
- PostgreSQL database persistence

---

# 🛠 Technology Stack

## Backend
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Hibernate ORM
- REST APIs
- Maven

## Database
- PostgreSQL

## Cache
- Redis

## Tools
- Git & GitHub
- Postman
- IntelliJ IDEA / Eclipse

---

# 🏗 System Architecture

```
              React Frontend
                    |
                    |
                REST API
                    |
                    |
          Spring Boot Backend
                    |
        -------------------------
        |                       |
   PostgreSQL Database      Redis Cache
```

---

# 📌 API Documentation

## 1. Create Short URL

### Request

```
POST /api/urls
```

### Example JSON

```json
{
    "originalUrl": "https://www.google.com",
    "customAlias": "google",
    "expiryDate": "2026-12-31T23:59:59"
}
```

### Response

```json
{
    "id": 1,
    "originalUrl": "https://www.google.com",
    "shortCode": "google",
    "shortUrl": "http://localhost:8080/api/urls/google",
    "clickCount": 0
}
```

---

# 2. Get All URLs

```
GET /api/urls
```

Returns all created URLs with analytics information.

Example:

```json
[
 {
    "id":1,
    "originalUrl":"https://www.google.com",
    "shortCode":"google",
    "clickCount":5
 }
]
```

---

# 3. Redirect Short URL

```
GET /api/urls/{shortCode}
```

Example:

```
GET /api/urls/google
```

Functionality:

- Redirects user to original URL
- Updates click count
- Updates last accessed time

---

# 4. Delete URL

```
DELETE /api/urls/{id}
```

Deletes a shortened URL from the system.

---

# 5. Analytics

```
GET /api/urls/analytics/{shortCode}
```

Returns:

- Original URL
- Short URL
- Click count
- Created date
- Last accessed date
- Expiry date

---

# ⚙️ Installation & Setup

## Clone Repository

```bash
git clone https://github.com/alekhyaGontla/LinkLite-backend.git
```

Move into project:

```bash
cd LinkLite-backend
```

---

# Database Configuration

Update:

```
src/main/resources/application.properties
```

Configure PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=admin
```

---

# Redis Configuration

Make sure Redis is running.

Configuration:

```properties
spring.data.redis.host=localhost
spring.data.redis.port=6379
```

---

# Run Application

Using Maven:

```bash
mvn spring-boot:run
```

Application runs on:

```
http://localhost:8080
```

---

# 📂 Project Structure

```
src/main/java/com/linklite

│
├── controller
│     └── UrlController.java
│
├── service
│     └── UrlService.java
│
├── serviceImpl
│     └── UrlServiceImpl.java
│
├── repository
│     └── UrlRepository.java
│
├── entity
│     └── Url.java
│
├── dto
│     ├── UrlRequest.java
│     └── UrlResponse.java
│
├── redis
│     └── RedisService.java
│
└── util
      └── HashGenerator.java
```

---

# 🔮 Future Enhancements

- User authentication and authorization
- JWT security implementation
- User dashboard
- Advanced analytics charts
- QR code generation
- Custom domains
- Cloud deployment
- Email notifications

---

# 🌐 Deployment Plan

Production deployment:

Frontend:
```
Vercel
```

Backend:
```
Render / AWS
```

Database:
```
PostgreSQL Cloud
```

Cache:
```
Redis Cloud
```

---

# 👨‍💻 Developer

**Alekhya Gontla**

Software Engineer | Java Full Stack Developer

---

# ⭐ Project Highlights

✔ Spring Boot REST API Architecture  
✔ PostgreSQL Database Integration  
✔ Redis Caching Implementation  
✔ URL Analytics Tracking  
✔ Custom Alias Support  
✔ Expiry Management  
✔ Full Stack Application Design  
