# Student Management Service

A simple Spring Boot REST API for managing students.

Project layout
- src/main/java - application source
- src/test/java - tests
- pom.xml - Maven configuration

Prerequisites
- Java 21
- Maven 3.8+
- A running PostgreSQL (or other) database if you use JPA persistence. Configure connection in `src/main/resources/application.properties`.

Build

```bash
# build the app
./mvnw clean package
# or run directly
./mvnw spring-boot:run
```

Run

```bash
# run the packaged jar
java -jar target/RestController-0.0.1-SNAPSHOT.jar
```

Default server
- Port: 8080

API Endpoints
- GET  /students            - list all students
- GET  /students/{id}       - get a single student by id
- POST /students            - create a new student (JSON body)
- PUT  /students/{id}       - replace/update a student (JSON body)
- PATCH /students/{id}      - partial update (JSON body containing fields to update)
- DELETE /students/{id}     - delete student by id

Example requests

```bash
# list
curl -s http://localhost:8080/students

# get
curl -s http://localhost:8080/students/1

# create
curl -s -X POST -H "Content-Type: application/json" -d '{"name":"John","email":"john@example.com"}' http://localhost:8080/students
```

Database configuration

Edit `src/main/resources/application.properties` to point to your database. Example common properties:

spring.datasource.url=jdbc:postgresql://localhost:5432/yourdb
spring.datasource.username=youruser
spring.datasource.password=yourpassword
spring.jpa.hibernate.ddl-auto=update

Notes / Troubleshooting
- ID type: If you use `GenerationType.UUID` the entity id must be a `java.util.UUID` (or appropriate column type). If the id field is `Long`, use `GenerationType.IDENTITY` or `AUTO` instead. If you see an error like "Unanticipated return type [java.lang.Long] for UUID conversion", that means the configured generator returns UUID but your id field is a Long.
- If git push fails, ensure you have a remote named `origin` and have proper credentials (SSH key or HTTPS login).

Tests

```bash
./mvnw test
```

License

This project is provided as-is.

