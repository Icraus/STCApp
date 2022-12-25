# STC Clinic App

This is a simple app that allows you to create a clinic and add patients to it.
## Technologies used:
- Java
- Spring Boot
- Hibernate Reactive
- Webflux
- Postgres
- Docker Compose
- GraphQL

## How to run the app
- Clone the repo
- Run ```gradlew bootJar```
- Run Postgres database with user postgres / password root
- Run `Schema.sql ` to create the database
- Run `docker-compose up` to start the database, and App
- Go to `http://localhost:8080/graphiql` to access the GraphQL playground