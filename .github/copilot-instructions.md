# Project Instructions — AML Pattern Detection Dashboard

## Purpose

This project is an **Anti-Money Laundering (AML) Pattern Detection Dashboard** that enables compliance teams to detect, investigate, and monitor suspicious transaction patterns across account networks. The system analyzes financial transaction data to identify potential money laundering activities such as structuring, velocity anomalies, and circular fund flows.

## Architecture Overview

```
┌─────────────────────────────────────────────────────────────────┐
│                     Angular 17+ Frontend                        │
│  ┌─────────────┐  ┌─────────────┐  ┌──────────────────────┐    │
│  │ AML Dashboard│  │Pattern Viewer│  │ Account Network     │    │
│  └─────────────┘  └─────────────┘  └──────────────────────┘    │
└────────────────────────────┬────────────────────────────────────┘
                             │ REST JSON API
┌────────────────────────────▼────────────────────────────────────┐
│                   Java 21 + Spring Boot 3                       │
│  ┌────────────┐  ┌────────────┐  ┌────────────┐  ┌──────────┐  │
│  │ Controllers│  │  Services  │  │ Rule Engine│  │  Loaders │  │
│  └────────────┘  └────────────┘  └────────────┘  └──────────┘  │
└────────────────────────────┬────────────────────────────────────┘
                             │ File I/O
┌────────────────────────────▼────────────────────────────────────┐
│               Static JSON Data Files                            │
│  accounts.json │ transactions.json │ aml-flags.json             │
└─────────────────────────────────────────────────────────────────┘
```

## Tech Stack & Constraints

| Layer    | Technology                              | Version |
|----------|-----------------------------------------|---------|
| Frontend | Angular (standalone components)         | 17+     |
| Backend  | Java + Spring Boot                      | 21 / 3  |
| Data     | Static JSON files                       | —       |
| API      | REST JSON                               | —       |

### Critical Constraints

- **NO database** — all data comes from static JSON files only
- **NO ORM** — do not use JPA, Hibernate, or Spring Data
- **NO graph/chart library** — render visualizations with plain HTML/CSS
- **NO Lombok** — use explicit constructors, getters, setters

## Data Source

All data is loaded from static JSON files located in `data/` directory:

| File                    | Description                                      |
|-------------------------|--------------------------------------------------|
| `accounts.json`         | Account entities with metadata                   |
| `transactions.json`     | Transaction records between accounts             |
| `transaction-edges.json`| Graph edges representing account relationships   |
| `aml-flags.json`        | Pre-computed AML flags and alerts                |

### JSON Schema Conventions

- All keys use `camelCase`
- Dates use ISO-8601 format: `"2026-04-16T10:30:00Z"`
- IDs are strings: `"accountId": "ACC-001"`
- Amounts are numbers with 2 decimal precision: `"amount": 15000.00`

## Code Style

### General

- Use descriptive, meaningful names
- Write self-documenting code with minimal comments
- Keep functions/methods focused on single responsibility
- Prefer explicit over implicit

### Commit Messages

Format: `<type>(<scope>): <description>`

Types: `feat`, `fix`, `refactor`, `docs`, `test`, `chore`

Example: `feat(rule-engine): add circular flow detection rule`

## Error Handling

- All API errors return structured response with `errors[]` array
- Use appropriate HTTP status codes (400, 404, 500)
- Log errors with contextual information
- Never expose stack traces to clients

## REST API Response Envelope

All API responses follow this structure:

```json
{
  "data": { },
  "meta": {
    "timestamp": "2026-04-16T10:30:00Z",
    "requestId": "uuid"
  },
  "errors": []
}
```

## Rule-Based AI Analysis

The system uses deterministic rule functions for AML detection:

| Rule Type          | Description                                        |
|--------------------|----------------------------------------------------|
| Velocity Check     | Detects unusual transaction frequency              |
| Structuring        | Identifies split transactions avoiding thresholds  |
| Circular Flow      | Finds money returning to origin through hops       |
| Amount Threshold   | Flags transactions exceeding defined limits        |

Rule matches are returned in API responses as `ruleMatches[]` array.

## Verification Commands

```bash
# Backend
mvn clean verify

# Frontend
ng build --configuration production

# API Health Check
curl http://localhost:8080/api/health

# Test JSON Loader Endpoint
curl http://localhost:8080/api/accounts
```

---

*Domain-specific instructions:*
- `.github/instructions/frontend.instructions.md` — Angular guidelines
- `.github/instructions/backend.instructions.md` — Java/Spring Boot guidelines
