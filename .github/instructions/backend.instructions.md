---
applyTo: "backend/**,backend/pom.xml,pom.xml"
model: claude-sonnet-4-20250514
---

# Backend Instructions — Java 21 + Spring Boot 3

## Purpose

This repository contains a Spring Boot backend for the AML Pattern Detection Dashboard. Treat the `backend/` module as the authoritative application source. The root `pom.xml` is the parent/aggregator POM.

If duplicate or legacy sources exist outside `backend/`, do not modify them unless the user explicitly asks.

## Canonical Module Layout

```text
backend/
├── src/main/java/com/aml/trustwatch/
│   ├── TrustwatchApplication.java
│   ├── controller/
│   ├── service/
│   ├── loader/
│   ├── rules/
│   ├── model/
│   │   └── response/
│   ├── config/
│   └── exception/
├── src/main/resources/
│   └── data/
├── src/test/java/com/aml/trustwatch/
└── pom.xml
```

## Core Principles

- Use Java 21, Spring Boot 3, and explicit imports only.
- Prefer simple, deterministic code over clever code.
- Keep controllers thin; keep business logic in services; keep file I/O in loaders.
- Use records for immutable DTOs and API response types.
- Use standard classes with explicit constructors, getters, and setters for mutable domain models.
- Use `BigDecimal` for money and `java.time` types for dates.
- Use constructor injection only.
- Fail fast on invalid configuration or unreadable data files.

## Hard Constraints

- No database or ORM of any kind: no JPA, Hibernate, Spring Data, MongoDB, H2, or JDBC persistence.
- No Lombok.
- No field injection (`@Autowired` on fields is forbidden).
- No wildcard imports.
- No raw or unchecked generic returns in application code.
- No stack traces or internal exception details in API responses.
- No frontend-only concerns in backend instructions.

## API Contract

All controller responses must use the API envelope:

```java
public record ApiResponse<T>(T data, ApiMeta meta, List<ApiError> errors) { }
```

Rules:

- Always return `ResponseEntity<ApiResponse<T>>` from controllers.
- Use `ApiResponse.success(...)` for successful responses.
- Use `ApiResponse.error(...)` through exception handling, not ad hoc controller try/catch blocks.
- Preserve consistent `meta.timestamp` and `meta.requestId` values for every response.

## Data Loading Rules

- Load all data from `src/main/resources/data/*.json` using `ClassPathResource` and Jackson `ObjectMapper`.
- Cache data at startup with `@PostConstruct`.
- Build lookup maps for ID-based access when needed.
- Return immutable views from getters (`Collections.unmodifiableList(...)` or `List.copyOf(...)`).
- Detect and fail on duplicate IDs when building lookup maps.
- Treat missing or malformed JSON as a startup/data-loading failure.
- Keep loader methods side-effect free after initialization.

## Naming and Modeling Rules

- Match JSON keys exactly using camelCase.
- Use `@JsonProperty` when Java naming and JSON naming differ, especially for boolean fields.
- Include a no-args constructor on mutable domain objects for Jackson.
- Keep response/error/meta objects in `model/response`.
- Keep domain entities separate from response records.

## Service Layer Rules

- Services must only orchestrate loaders, rule engine components, and domain transformations.
- Services must not know about HTTP, status codes, or serialization.
- Throw `ResourceNotFoundException` for missing domain objects.
- Validate required inputs early and fail with meaningful exceptions.
- Keep methods small and deterministic.

## Controller Rules

- Controllers must be thin adapters over services.
- Use clear `@RequestMapping` paths and specific `@GetMapping`, `@PostMapping`, etc.
- Use meaningful `@PathVariable` and `@RequestParam` names, with defaults only when appropriate.
- Do not add business logic or data access to controllers.
- Do not create endpoints that conflict with static resources or browser fallback routes.
- If an endpoint shape changes, update tests and documentation together.

## Rule Engine Rules

- Use deterministic rules only; do not introduce ML, LLM, or network calls.
- Rules should operate on cached domain data and return `Optional<RuleMatch>`.
- Keep each rule focused on one AML signal.
- Avoid time-dependent behavior unless explicitly needed; prefer injected clocks if a rule depends on time.

## Exception Handling Rules

- All custom exceptions must extend `RuntimeException`.
- Use a single `@RestControllerAdvice` to map exceptions into `ApiResponse.error(...)`.
- Return 400 for invalid input, 404 for missing resources, and 500 for unexpected failures.
- Log contextual details server-side, but never log sensitive account data.
- Never expose stack traces or internal class names to clients.
- Add a 404 handler for unmatched API paths when needed so API-only requests do not leak framework errors.

## Testing Rules

- Prefer unit tests with JUnit 5, Mockito, and AssertJ.
- Use `@ExtendWith(MockitoExtension.class)` for pure unit tests.
- Use constructor injection in tests as well.
- Mock only external dependencies; avoid over-mocking domain logic.
- Test happy paths, validation failures, and not-found/error cases.
- Add tests for every changed service and any controller behavior that changed.
- Use Spring integration tests only when verifying wiring, Jackson configuration, or request/response mapping.

## Verification Commands

Run commands from the repository root unless a module-specific command is required.

```bash
mvn clean verify
mvn -pl backend test
mvn -pl backend spring-boot:run
```

## Quality Checklist

Before finishing any backend change, verify:

- [ ] The change is in `backend/` unless the user explicitly requested otherwise.
- [ ] Imports are explicit.
- [ ] No Lombok or ORM annotations were added.
- [ ] All mutable models have getters, setters, and a default constructor.
- [ ] Controllers return `ResponseEntity<ApiResponse<T>>`.
- [ ] JSON field names match the data files.
- [ ] All dates use `Instant` or another `java.time` type.
- [ ] All money uses `BigDecimal`.
- [ ] New or modified services have tests.
- [ ] `mvn clean verify` was run or a narrower equivalent was run for the touched module.

## Reporting Format

When you finish a task, report:

- Files created or modified
- Verification results
- API endpoints added or changed
- Remaining work or dependencies

## References

- Workspace instructions: `.github/copilot-instructions.md`
- Domain instructions: `.github/instructions/backend.instructions.md`
- Data files: `backend/src/main/resources/data/*.json`
