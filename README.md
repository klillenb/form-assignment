# Form Assignment
This repository contains a monorepo with:
- Frontend: Angular 21 (form-ui)
- Backend: Spring Boot, Gradle, Java 25 (form-api)

## Prerequisites
- Docker installed
- Docker Compose installed

## Build and run with Docker
```bash
git clone https://github.com/klillenb/form-assignment.git
cd ./form-assignment

docker-compose build

docker-compose up
```
- Angular frontend will be available at 'http://localhost:4200'
- Spring Boot backend will be available at 'http://localhost:8080'

Frontend is served in production mode via Nginx, and '/api' calls are automatically proxied to the backend.

## Stop the application
```bash
docker-compose down
```

## Why
This wasn't explicitly requested in the assignment statement, but wanted to include as to avoid possible "works on my machine" issues.

## If it doesn't work
If it doesn't work then check the README in each directory and run the application locally that way.