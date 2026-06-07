# Healthcare Microservices - Security Setup Guide

## Architecture Overview
```
Client (Swagger/Postman/Browser)
         ↓
   API Gateway (8080)
         ↓
   ┌─────┴──────────────────────┐
   │                            │
JWT Auth Service (8085)   OAuth2 Auth Service (8086)
   │                            │
   └─────────┬──────────────────┘
             ↓
       Eureka Server (8761)
             ↓
   ┌─────────┴─────────┐
   │                   │
Patient Service    Appointment Service
   (8081)              (8082)
```

---

## Project 1: jwt-auth-service (Port 8085)

### What it does:
- Register new users → returns JWT token
- Login existing users → returns JWT token
- Validate token → used by API Gateway

### How to use in Swagger:
1. Run jwt-auth-service
2. Open: http://localhost:8085/swagger-ui/index.html
3. POST /auth/register → register new user
4. POST /auth/login → get JWT token
5. Click "Authorize" button in Swagger
6. Enter: Bearer <your_token>
7. Now access /auth/test

### Register Request:
```json
{
  "username": "anugna",
  "password": "password123",
  "email": "anugna@gmail.com",
  "role": "USER"
}
```

### Login Request:
```json
{
  "username": "anugna",
  "password": "password123"
}
```

### Response (both):
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "username": "anugna",
  "role": "ROLE_USER",
  "message": "Login successful!"
}
```

### Use token in Postman:
- Headers → Authorization → Bearer <token>

---

## Project 2: oauth2-auth-service (Port 8086)

### What it does:
- Login with Google → returns JWT token
- Login with GitHub → returns JWT token
- All microservices protected via this token

### Setup OAuth2 Credentials:

#### Google:
1. Go to: https://console.cloud.google.com
2. Create project → Enable Google+ API
3. Credentials → OAuth2 Client ID
4. Redirect URI: http://localhost:8086/login/oauth2/code/google
5. Copy Client ID and Secret → paste in application.properties

#### GitHub:
1. Go to: https://github.com/settings/developers
2. New OAuth App
3. Callback URL: http://localhost:8086/login/oauth2/code/github
4. Copy Client ID and Secret → paste in application.properties

### How to use:
1. Run oauth2-auth-service
2. Open browser: http://localhost:8086/oauth2/authorization/google
3. Login with Google
4. Get JWT token in response
5. Use token to access all microservices via API Gateway

---

## Database Setup (MySQL)
```sql
CREATE DATABASE authdb;
CREATE DATABASE oauthdb;
```

---

## Starting Order:
```
1. MySQL        → start MySQL service
2. Eureka       → http://localhost:8761
3. jwt-auth     → http://localhost:8085
4. oauth2-auth  → http://localhost:8086
5. patient      → http://localhost:8081
6. appointment  → http://localhost:8082
7. api-gateway  → http://localhost:8080
```

---

## Testing via API Gateway (Port 8080):
```
POST http://localhost:8080/auth/register  → register
POST http://localhost:8080/auth/login     → login
GET  http://localhost:8080/patients       → get patients (needs token)
GET  http://localhost:8080/appointments   → get appointments (needs token)
```

---

## Roles Available:
- ROLE_USER
- ROLE_ADMIN
- ROLE_DOCTOR
- ROLE_PATIENT
