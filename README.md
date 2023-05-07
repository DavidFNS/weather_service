# Weather service
Spring WebFlux application for getting weather info of cities

### App launch requirements:
- Installed Java version 17
- Set datasource parameters(url, username, password) in application.properties
- PostgreSQL server for database
- Used h2 cache for integration tests
- 8787 port must be empty or change it to another port

### Used technologies:
- Java 17
- Liquibase
- PostgreSQL
- Spring Boot
- Spring WebFlux
- Spring Security
- JUnit
- WebTestClient

## NOTE:
- If you want to run the program, then comment these properties from the file application.properties:
  - spring.profiles.active=testdev  
  - spring.profiles.default=testdev

- Run swagger-ui with URL: localhost:8787/swagger-ui
 
 