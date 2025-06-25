# Developed By Mohammed HANAFI

Note: I chose to organize the modules by technical layers (controller, service, repository) since this project focuses on a single business domain.
However, in real-world projects with multiple domains, it is recommended to structure the code using Domain-Driven Design (DDD), with modules organized by business context for better scalability and maintainability.

## Test
- Find the file lab-book-collection-api-0.0.1-SNAPSHOT.jar in root folder
- And Launch : java -jar lab-book-collection-api-0.0.1-SNAPSHOT.jar --server.port=9090
- And test via this [postman collection](./Test_books_API.postman_collection.json)

## Technologies Used

- Spring Boot 3.5.3
- Spring Data JPA
- H2 Database
- Spring WebFlux (for external API calls)
- JUnit 5 (for testing)
