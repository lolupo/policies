# Tinubu Policies Microservice

## Project Overview

This repository contains a full-stack insurance policies management application. It provides RESTful APIs to create,
retrieve, update, and manage insurance policies, supporting business logic and persistence. The service is designed for
extensibility, maintainability, and scalability, following modern software engineering principles.

## Technologies Used

- **Backend:** Java, Spring Boot
- **Frontend (SPA):** React, TypeScript, Vite
- **Database:** PostgreSQL
- **Migrations:** Flyway

## Generation

The SPA was generated using **GitHub Copilot** in agent mode, following strict software engineering and architectural
rules.

## Features

- CRUD operations for insurance policies
- Policy status management
- Database migration scripts (Flyway)
- Clean separation of domain, application, and infrastructure layers
- Integration and unit tests
- Modern UI for insurance policies management (SPA)
- Routing, pagination, creation, edition, and detail views in the SPA

## Deployment

### Prerequisites

- Java 21
- Maven
- Docker
- PostgreSQL
- React (for the frontend SPA)

### Technical Stack

- **Spring Boot:** Robust framework for building production-ready Java applications.
- **PostgreSQL:** Reliable and scalable relational database for policy data.
- **Flyway:** Automated and versioned database migrations.
- **React + TypeScript + Vite:** Modern SPA for frontend.

### Deployment

The application is deployed using Docker Compose, which orchestrates the following services:

- **API Service:** Runs the Spring Boot application, exposing RESTful endpoints for policy management.
- **Database Service:** Starts a PostgreSQL container for persistent storage of insurance policies.
- **Migration Service:** Flyway migrations are executed automatically on application startup to ensure the database
  schema is up to date.
- **SPA Service:** Runs the React frontend, accessible via `/policies/dashboard`.

To start the application and its dependencies, run:

```bash
  docker-compose up --build
```

This command builds the Docker images and starts all necessary containers. The API will be accessible at
`http://localhost:8080` and the SPA at `http://localhost/policies/dashboard` (the frontend port is **80**).

## Usage

You can interact with the API using any HTTP client (e.g., Postman, curl) or directly from your browser using the
Swagger UI:

- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- or [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

The SPA is accessible at:

- [http://localhost/policies/dashboard](http://localhost/policies/dashboard)

## Endpoints

The microservice exposes the following endpoints for insurance policy management:

| Method | Endpoint           | Description                                   |
|--------|--------------------|-----------------------------------------------|
| GET    | /api/policies      | List policies (paginated, params: page, size) |
| POST   | /api/policies      | Create a new insurance policy                 |
| GET    | /api/policies/{id} | Retrieve a specific insurance policy          |
| PUT    | /api/policies/{id} | Update an existing insurance policy           |

### Pagination

- The listing endpoint `/api/policies` supports pagination via `page` (default: 0) and `size` (default: 10) query
  parameters.
- The response includes metadata: `content`, `page`, `size`, `totalElements`, `totalPages`.

### Example Paginated Response

```json
{
  "content": [
    ...
  ],
  "page": 0,
  "size": 10,
  "totalElements": 100,
  "totalPages": 10
}
```

## Frontend (React SPA)

A React SPA is provided in the `spa/` folder. It consumes the API and supports:

- Listing policies with pagination
- Creating, reading, and editing policies

To run the SPA:

```bash
  cd spa
  npm install
  npm run dev
```

Or use Docker Compose to run both backend and frontend together.

## Development Strategy

This project follows strict software engineering rules:

- **Domain-Driven Design (DDD):**
    - The domain layer models the main business concepts and rules, like InsurancePolicy and PolicyStatus.
    - Business logic is kept in domain objects, separate from technical details.
    - Aggregates, entities, and value objects help structure the domain and keep boundaries clear.

- **Object Calisthenics:**
    - Classes are small and focused on one responsibility.
    - Domain-specific types and value objects are used instead of primitives.
    - Collections are wrapped in objects to encapsulate behavior.
    - Getters/setters are minimized; objects expose business actions through methods.

- **Clean Architecture:**
    - The code is split into domain, application, and infrastructure layers.
    - The domain layer is independent from frameworks and technical details.
    - The application layer coordinates use cases.
    - The infrastructure layer manages persistence and external systems.

- **Consistent Coding Style:** Naming and formatting are uniform for clarity.

## Domain Model Design Choice

We use both a domain object `InsurancePolicy` and a persistence entity. While using only the entity would be simpler, we
chose to strictly follow DDD, Clean Architecture, and Object Calisthenics. This keeps business logic pure and separate
from technical concerns, and makes the code easier to maintain and evolve. However, it does mean more mapping code
between the domain object and the entity.

### Persistence-generated fields

The `id`, `creationDate`, and `updateDate` fields are automatically generated by the persistence layer (e.g., the
database or JPA/Hibernate). This is a common approach when using auto-incremented primary keys and timestamp columns
managed by the database. If we were using a UUID as the identifier, it would be possible to generate these values
directly in the domain layer, which would further decouple the domain from infrastructure concerns.

### Data Deletion Strategy

To manage policy deletion without removing rows from the database, we added an `expiry_date` field to the
`insurance_policy` table. When a policy is "deleted", its `expiry_date` is set. The API only returns policies where
`expiry_date` is `NULL`, ensuring soft deletion and full auditability.

## API Versioning

API versioning is implemented to ensure backward compatibility as the API evolves. By versioning the API, we can
introduce new features or change existing behavior without breaking existing clients. In this project, versioning is
handled via the HTTP `Accept` header using a custom media type (e.g., `application/vnd.tinubu.policies.v1+json`).

**How it works:**

- Endpoints that require versioning specify the `produces` attribute in their controller annotations (e.g.,
  `@GetMapping(produces = "application/vnd.tinubu.policies.v1+json")`).
- Clients must include the correct `Accept` header in their requests to access a specific version of the API.
- This approach keeps URLs clean and allows multiple versions to coexist, each with its own representation and behavior.

**Example:**

```http
GET /api/policies
Accept: application/vnd.tinubu.policies.v1+json
```

**Summary:**

- Versioning via the `Accept` header ensures safe evolution of the API without breaking existing consumers.

## Error Handling

This API uses a global exception handler to ensure all errors are returned in
the [RFC 7807](https://tools.ietf.org/html/rfc7807) `application/problem+json` format. The main error scenarios covered
are:

- **Validation errors (400 Bad Request):** Triggered when DTO constraints are violated (e.g., missing required fields).
  The response includes a detailed error message and all standard RFC 7807 fields.
- **Illegal arguments (400 Bad Request):** Triggered when an invalid argument is passed to the API (e.g., negative ID).
  The response includes a "Bad Request" message in problem+json format.
- **Generic exceptions (500 Internal Server Error):** Any unexpected error is returned as a 500 error in problem+json
  format. Note: due to validation, it is not possible to trigger a NullPointerException via the API.

All error responses are consistent and designed for easy client-side parsing.

## Testing Strategy

- Integration tests use only real API endpoints (e.g., `/api/policies`, `/api/policies/{id}`).
- Error handling is tested by sending invalid data or parameters to trigger validation or illegal argument errors.
- Only realistic error scenarios are tested. It is not possible to trigger generic exceptions (like
  NullPointerException) via the API due to validation constraints.
- All code and documentation must be in English.
- Run all tests with `mvn test` before committing changes. All tests must pass and there should be no compiler
  warnings.

## Latest Integration Tests

The following integration tests have been added to ensure robust API behavior and business rule enforcement:

- **shouldReturnBadRequestWhenPathIdDiffersFromDtoId**  
  Verifies that a PUT request to update a policy returns a 400 Bad Request (RFC 7807 format) if the path id and DTO id
  are different.

- **shouldNotUpdateCreationDateOnPolicyUpdate**  
  Ensures that the creation date of a policy cannot be modified during an update; the original creation date is always
  preserved.

These tests improve coverage for edge cases and guarantee compliance with business rules and RESTful standards.

---
