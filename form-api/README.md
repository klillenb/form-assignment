# Forms API

## Technologies
- Java 25
- Spring Boot 3.5.9
- liquibase for DB migrations
- H2 as in-memory DB (handles schema migration and seeding)
- Logbook & Logstash for HTTP request/response logging

## Requirements
- Java 22 SDK
- Gradle 8+

## Setup
1. Clone the repo
```bash
git clone https://github.com/klillenb/form-assignment.git
cd ./form-assignment/form-api
```
2. Build the project
```bash
./gradlew build
```
3. Run the application
```bash
./gradlew bootRun
```
Application will start on default port 8080.

## What could've been done better
- Not store H2 credentials (default values) in application.yaml
- Could've used some kind of level attribute on DB records to indicate level, makes it more human-readable to understand depth, currently sectors table references itself
- Add authentication and authorization.
- Add tests for custom queries in repositories.
- Could add some exception handler for better control.
- Sectors data is coming from FormController, which currently is fine imo, but if this application were to be extended to add sectors create, update, delete actions, then it would need to be extracted to a separate controller.
- If the originally provided select tag values in HTML were important and/or have to be kept, then could add new field to sectors table which stores that value. Most likely they represent some kind of ids, but it would be unwise to start hardcoding them in a new system as PKs.