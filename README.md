# GitHub Copilot Agent-Driven Development Workshop

A hands-on workshop where participants build a project prototype entirely through an agentic workflow — using GitHub Copilot's Ask, Plan, and Agent modes together with specialist agents, reusable skills, and a strict TDD loop. No implementation code is provided; everything is produced through structured AI collaboration following [AGENTIC_WORKFLOW.md](AGENTIC_WORKFLOW.md).

---

## Contents

- [Learning Goals](#learning-goals)
- [Choosing Your Project](#choosing-your-project)
- [Getting Started](#getting-started)
- [Project 1 — Transaction Risk Alert Dashboard](#project-1--transaction-risk-alert-dashboard)
- [Project 2 — AML Pattern Detection Dashboard](#project-2--aml-pattern-detection-dashboard)
- [Project 3 — Legacy Banking System Modernisation](#project-3--legacy-banking-system-modernisation-migrate--modernize-track)
- [Mock Data Strategy](#mock-data-strategy)
- [Recommended Stack](#recommended-stack)
- [Workshop Flow](#workshop-flow)
- [Agentic Workflow Reference](#agentic-workflow-reference)

---

## Learning Goals

- **Use the Right Mode**: Know when to reach for Ask (sparring partner), Plan (review before execution), or Agent (direct execution), and why the distinction matters
- **Blueprint Before Building**: Use AI to produce `BLUEPRINT.md` and `developer_todo.md` before writing a single line of implementation code
- **Author Specialist Agents**: Create focused agents with clearly defined responsibilities, minimal tool sets, and explicit domain boundaries
- **Replace Guesswork with Skills**: Build reusable skills that give agents deterministic capabilities — consistent backlog item structure, correct test execution, reliable project state
- **Implement End-to-End with TDD**: Drive one backlog item from data layer through to a tested UI component using the strict test-first loop

---

## Choosing Your Project

| | Project 1 | Project 2 | Project 3 |
|---|---|---|---|
| **Name** | Transaction Risk Alert Dashboard | AML Pattern Detection Dashboard | Legacy System Modernisation |
| **Data Source** | Static JSON data files (provided) | Static JSON data files (provided) | External open-source starter repos (choose one) |
| **Data Focus** | Per-account transaction risk | Account network AML patterns | Code migration |
| **AI Integration** | Display only | Display + rule-based analysis | Migration planning + code generation |
| **Best For** | Learning the agentic workflow basics | Complex domain modelling and graph data | Modernising legacy systems with AI |

---

## Getting Started

1. **Choose your project** from the table above
2. **Review the mock data schema** for your chosen project (defined in each project section below)
3. **Follow the agentic workflow**: Use [AGENTIC_WORKFLOW.md](AGENTIC_WORKFLOW.md) as your step-by-step guide through blueprinting, instruction authoring, specialist agent creation, and feature implementation. All participants follow the same workflow — the project you chose determines the domain.

---

## Project 1 — Transaction Risk Alert Dashboard

### Objective

Build a web application that helps a bank's operations team make rapid risk decisions by visualising transaction risk indicators for customer accounts. Mock data is provided as static JSON files — no backend to build.

### Goal

Practice the agentic workflow to develop a complete, functional web application from initial specification through to a working frontend — using the provided `data/transactions.json` as your data source.

### Scenario

You are a developer at a fintech company. The fraud operations team needs a tool to assess account risk at a glance. Suspicious transaction activity is rising, and the team wants to see whether an account is **GREEN** (low risk), **YELLOW** (review required), or **RED** (block/escalate). They need to see the key fraud signals driving the score and how transaction behaviour has changed over the past 24 hours.

### Required Features

#### 1. Account Search and Data Retrieval
- Accept input by account ID or customer name
- Load mock transaction data from the provided `data/transactions.json` file
- Display the current fraud indicator signals (velocity anomaly, geo-anomaly, unusual merchant category, high-value spike)
- Handle missing accounts and empty transaction histories gracefully

#### 2. Risk Status Visualisation
- Calculate and display the overall account risk tier: **GREEN / YELLOW / RED**
- Apply colour-coded indicators based on the weighted fraud signals
- Show the top contributing risk factors in plain language
- Display account metadata and the timestamp of the most recent assessment

#### 3. 24-Hour Transaction Timeline
- Present a timeline of transactions over the past 24 hours
- Visualise transaction amount trends and risk score changes over time
- Highlight time windows with elevated suspicion
- Show transaction count per hour as a bar or sparkline chart

### Mock Data Schema

```
Transaction
  transactionId    : UUID
  accountId        : String
  customerName     : String
  amount           : BigDecimal
  currency         : String (ISO 4217)
  merchantName     : String
  merchantCategory : Enum (RETAIL, GAMBLING, CRYPTO_EXCHANGE, UTILITIES, TRAVEL, OTHER)
  originCountry    : String (ISO 3166)
  destinationCountry : String (ISO 3166)
  riskScore        : Integer (0–100)
  fraudIndicators  : List<Enum> (VELOCITY_ANOMALY, GEO_ANOMALY, UNUSUAL_MERCHANT, HIGH_VALUE_SPIKE)
  timestamp        : Instant
  status           : Enum (APPROVED, PENDING_REVIEW, BLOCKED)
```

Seed data: Provided as `data/transactions.json`.

---

## Project 2 — AML Pattern Detection Dashboard

### Objective

Build a compliance monitoring dashboard that helps an Anti-Money Laundering (AML) team visualise suspicious transaction chains and account networks, detect known AML typologies, and prioritise which accounts requiring a Suspicious Activity Report (SAR) or further investigation. Mock data is provided as static JSON files — no backend to build.

### Goal

Practice the agentic workflow to develop a complete, functional web application. Learn to decompose a complex compliance domain into manageable epics and tasks while maintaining consistency through shared documentation artefacts (`BLUEPRINT.md`, `developer_todo.md`). Use the provided `data/accounts.json` and `data/transaction-edges.json` as your data source.

### Scenario

You are a developer at a bank's financial crime unit. The compliance team uses vehicle-mounted sensors to... — wait, wrong domain. Here: the compliance team monitors transaction flows across account networks. They need a dashboard to visualise transaction chains in real time, identify AML pattern flags (structuring, layering, round-tripping), and manage a priority queue of accounts for review. The tool should help them decide which accounts need immediate action before reporting deadlines.

### Required Features

#### 1. Transaction Chain Display
- Load and display paginated transaction histories for individual accounts from the provided JSON data
- Show relationship metadata: sending account, receiving account, intermediary hops
- Filter by date range, transaction type, and AML flag status
- Display account network as a graph (nodes = accounts, edges = transactions)
- Handle pagination of large datasets (30 records per page)

#### 2. AML Pattern Visualisation
- Display detected AML typology indicators per account:
  - **Structuring**: multiple transactions just below reporting thresholds
  - **Layering**: rapid movement through intermediary accounts
  - **Round-tripping**: funds leaving and returning to the same account
  - **Shell network**: transactions concentrated through newly opened accounts
- Show a confidence score (0–100%) for each detected pattern
- Use colour-coded severity indicators (LOW / MEDIUM / HIGH / CRITICAL)
- Present time-series charts showing pattern intensity changes over a rolling 30-day window

#### 3. Compliance Decision Support
- Display recommended compliance actions per account:
  - File SAR (Suspicious Activity Report)
  - Enhanced Due Diligence (EDD) review
  - Temporary account freeze
  - Escalate to Financial Intelligence Unit (FIU)
- Show a ranked priority queue of accounts requiring action
- Highlight threshold breaches (e.g., transactions aggregating above €10,000 within 24 hours)
- Present time-series data showing how flagged activity has evolved

### Mock Data Schema

```
Account
  accountId       : UUID
  customerName    : String
  iban            : String
  accountType     : Enum (PERSONAL, BUSINESS, OFFSHORE)
  openedDate      : LocalDate
  riskRating      : Enum (LOW, MEDIUM, HIGH, PEP)
  amlFlags        : List<AmlFlag>

AmlFlag
  flagId          : UUID
  typology        : Enum (STRUCTURING, LAYERING, ROUND_TRIPPING, SHELL_NETWORK)
  confidenceScore : Integer (0–100)
  severity        : Enum (LOW, MEDIUM, HIGH, CRITICAL)
  detectedAt      : Instant
  recommendedAction : Enum (SAR, EDD, FREEZE, FIU_ESCALATION)

TransactionEdge
  edgeId          : UUID
  fromAccountId   : UUID
  toAccountId     : UUID
  amount          : BigDecimal
  currency        : String
  hops            : Integer
  timestamp       : Instant
  isFlagged       : Boolean
```

Seed data: 50 accounts with relationship edges forming a directed transaction graph; 10 accounts pre-configured with embedded suspicious patterns across all four typologies. Provided as `data/accounts.json` and `data/transaction-edges.json`.

---

## Project 3 — Legacy System Modernisation (Migrate & Modernize Track)

### Objective

Use GitHub Copilot as your migration co-pilot to modernise a real open-source codebase. Clone one of the three provided starter repositories, analyse it with Copilot's help, generate a migration plan, execute incremental refactoring steps, and validate the modernised application — all driven by isolated agent tasks.

### Goal

Practice the agentic workflow in a real-world migration context. Rather than building from scratch, you will analyse an actual public codebase, identify what needs to change, and drive the transformation layer by layer using structured AI collaboration.

### Starter Repositories — Choose One

| Option | Repository | Source Stack | Suggested Migration Target |
|---|---|---|---|
| **A** | [ContosoUniversity](https://github.com/jasontaylordev/ContosoUniversity) | .NET 6 / ASP.NET Razor Pages / EF Core / C# | .NET 8 Minimal API + React frontend (decoupled SPA) |
| **B** | [uportal-messaging](https://github.com/UW-Madison-DoIT/uportal-messaging) | Java / Spring Boot 2.x / Maven / JUnit 4 | Spring Boot 3.x + Spring Data JPA + JUnit 5 + Actuator |
| **C** | [docraptor-java](https://github.com/DocRaptor/docraptor-java) | Java / OpenAPI-generated client / Gradle | Java 21 + Spring Boot 3.x wrapper service + REST API |

Fork your chosen repository and use it as your migration baseline.

### What Makes This Track Different

- **Migration-first mindset**: You start from existing public code, not a blank slate
- **AI-assisted analysis**: Use Copilot to understand source patterns before changing them
- **Incremental refactoring**: Each agent task moves one layer forward without breaking the others
- **Validation at every step**: Copilot generates tests before and after each migration step

### Migration Guide

Follow [MIGRATION.md](MIGRATION.md) as your step-by-step guide throughout this track. The table below maps each deliverable to the relevant section:

| Deliverable | MIGRATION.md section |
|---|---|
| Source code analysis + migration plan | [Part A — Plan Migration Strategy](MIGRATION.md#part-a-plan-migration-strategy-with-ai) |
| Understand architecture and data flows | [Part B — Understand Source Code](MIGRATION.md#part-b-understand-source-code) |
| Set up target environment and test plan | [Part C — Setup Target Environment & Test Plan](MIGRATION.md#part-c-setup-target-environment--test-plan) |
| Incremental code migration by layer | [Part D — Migrate Code with AI](MIGRATION.md#part-d-migrate-code-with-ai) |
| Validate, test, and iterate | [Part E — Validate & Iterate](MIGRATION.md#part-e-validate--iterate) |
| Automate with agents and prompt files | [Automating Migrations](MIGRATION.md#-automating-migrations-with-custom-agents--prompt-files) |

### Required Deliverables

#### 1. Source Code Analysis
- Fork and clone your chosen starter repository
- Use Copilot to audit the codebase and produce a `migration-plan.md` — follow [Part A](MIGRATION.md#part-a-plan-migration-strategy-with-ai) and [Part B](MIGRATION.md#part-b-understand-source-code)
- Identify deprecated APIs, outdated dependencies, missing test coverage, and architectural gaps
- Map existing endpoints and data models to their modern equivalents
- Document breaking changes and migration risks

#### 2. Core Migration
- Upgrade the build tool configuration and dependency versions
- Introduce a clean layered architecture appropriate to the target stack
- Expose or modernise the API surface with proper request/response contracts — follow [Part C](MIGRATION.md#part-c-setup-target-environment--test-plan) and [Part D](MIGRATION.md#part-d-migrate-code-with-ai)
- Replace deprecated patterns with current framework equivalents
- Add health and info endpoints (Actuator or equivalent)

#### 3. Modernisation & Observability
- Add structured logging with contextual fields (request ID, relevant domain IDs)
- Add global error handling with standardised problem details (RFC 7807 or equivalent)
- Write integration tests covering the migrated endpoints — follow [Part E](MIGRATION.md#part-e-validate--iterate)
- Produce a `migration-report.md` summarising what changed, what was removed, and what was improved

### Technical Migration Path by Option

**Option A — ContosoUniversity (.NET)**

| Phase | From | To |
|---|---|---|
| Architecture | Razor Pages monolith | Minimal API backend + React SPA |
| Data access | EF Core + DbContext in pages | Repository pattern + EF Core 8 |
| API layer | Page handlers | `app.MapGroup()` Minimal API endpoints |
| Error handling | Page-level try/catch | `IProblemDetailsService` middleware |
| Testing | xUnit + in-process | xUnit + `WebApplicationFactory` |

**Option B — uportal-messaging (Java)**

| Phase | From | To |
|---|---|---|
| Boot version | Spring Boot 2.x | Spring Boot 3.x |
| Data access | JPA / JDBC (older patterns) | Spring Data JPA + `@Repository` |
| REST layer | `@RestController` (review & modernise) | `@RestController` + OpenAPI docs |
| Error handling | Ad-hoc exception handling | `@ControllerAdvice` + RFC 7807 |
| Testing | JUnit 4 | JUnit 5 + `@SpringBootTest` |
| Observability | Basic logging | Spring Actuator + structured logging |

**Option C — docraptor-java (Java SDK → Service)**

| Phase | From | To |
|---|---|---|
| Structure | OpenAPI-generated client library | Spring Boot 3.x wrapper service |
| Java version | Java 8 | Java 21 |
| Build | Gradle (legacy config) | Gradle 8 + updated dependencies |
| API layer | None (library only) | `@RestController` REST endpoints |
| Testing | Basic JUnit | JUnit 5 + `@SpringBootTest` |
| Observability | None | Spring Actuator + structured logging |

---

## Mock Data Strategy

**Projects 1 & 2** use pre-generated static JSON files — no server or database setup required.

| Concern | Approach |
|---|---|
| Data files | Provided in `data/` at the repo root — load via `fetch('./data/<file>.json')` |
| Realistic values | Pre-generated: realistic IBANs, names, amounts, merchant names, countries |
| Embedded patterns | Suspicious patterns, risk spikes, and AML typologies baked into the seed data intentionally |
| No server needed | All data is read directly from disk — no Spring Boot, no database |

**Project 3** uses a forked external open-source repository as its baseline — fork your chosen starter repo (see Project 3 section) and no separate data files are needed.

---

## Recommended Stack

**Projects 1 & 2 — Frontend**

| Layer | Technology |
|---|---|
| Framework | Your choice — React, Vue, vanilla JS, or any frontend stack |
| Data | Static JSON files via `fetch()` — no API calls to a server |
| Testing | Vitest / Jest or equivalent |
| Build | Vite or equivalent |

**Project 3 — Migration (stack depends on chosen starter)**

| Option | Target Framework | Data | REST | Testing | Build |
|---|---|---|---|---|---|
| A (ContosoUniversity) | .NET 8 Minimal API + React | EF Core 8 | `app.MapGroup()` | xUnit + WebApplicationFactory | .NET CLI |
| B (uportal-messaging) | Spring Boot 3.x | Spring Data JPA + H2 | `@RestController` | JUnit 5 + Mockito | Maven |
| C (docraptor-java) | Spring Boot 3.x | N/A (HTTP client) | `@RestController` | JUnit 5 + Mockito | Gradle 8 |

---

## Workshop Flow

| Phase | Activity | Approx. Duration |
|---|---|---|
| 0 | Intro: agentic workflow concepts, Copilot agent modes, workspace setup | 20 min |
| 1 | Project selection + data schema walkthrough | 10 min |
| 2 | **Blueprint phase**: Architect agent produces `BLUEPRINT.md` (tech decisions, data model, API design) — hard stop at 30 min | 30 min |
| 3 | **Planning phase**: PM agent generates `developer_todo.md`, selects first backlog item | 20 min |
| 4 | **Implementation**: specialist agents — data layer → logic → first UI component (one backlog item end-to-end) | 90 min |
| 5 | **Test phase**: Test Engineer writes and runs failing tests → implementation → green | 30 min |
| 6 | Demo + retrospective: what did the agents do well, where did they need redirecting? | 20 min |

---

## Agentic Workflow Reference

Follow [AGENTIC_WORKFLOW.md](AGENTIC_WORKFLOW.md) for the full step-by-step guide covering all phases from blueprinting through to feature implementation. It includes mode guidance (Ask / Plan / Agent), time estimates, and a design checklist to review at the end of each phase.

**Key artefacts to produce in every project:**

| File | Produced by | Purpose |
|---|---|---|
| `BLUEPRINT.md` | Architect agent (Ask mode) | Technical decisions, data model, API surface, constraints |
| `developer_todo.md` | Planning agent (Ask mode) | Phased task list with acceptance criteria per task |
| `.github/copilot-instructions.md` | Agent mode | Universal project conventions |
| `.github/agents/<name>.agent.md` | Agent mode | Specialist agent definitions |
| `README.md` | Developer agent | Running instructions, architecture summary |
