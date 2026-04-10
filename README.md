# GitHub Copilot Agent-Driven Development Workshop


---

## Learning Goals

- **Adopt an Agentic Workflow**: Practice building a complete prototype using a Copilot agent as your primary development partner
- **Practice Blueprint Creation**: Collaborate with an AI Architect to establish foundational technical decisions before writing any code
- **Practice Thread Isolation**: Manage complexity by assigning distinct, single-purpose tasks to separate, isolated agent threads
- **Utilise AI for Planning**: Use the agent not just for coding, but as a strategic partner to generate a project roadmap and `TODO.md`
- **Task Decomposition**: Break the project goal into small, well-defined tasks that a focused agent can complete in a single thread

---

## Choosing Your Project

| | Project 1 | Project 2 | Project 3 |
|---|---|---|---|
| **Name** | Transaction Risk Alert Dashboard | AML Pattern Detection Dashboard | Legacy Banking System Modernisation |
| **Complexity** | Beginner–Intermediate | Intermediate–Advanced | Advanced |
| **Data Source** | Mock REST API (Spring Boot) | Mock REST API (Spring Boot) | Provided legacy starter codebase |
| **Data Focus** | Per-account transaction risk | Account network AML patterns | Account + transaction data migration |
| **AI Integration** | Display only | Display + rule-based analysis | Migration planning + code generation |
| **Best For** | Learning the agentic workflow basics | Complex domain modelling and graph data | Modernising legacy systems with AI |

**Recommendation:**
- New to agentic workflows? Start with **Project 1**
- Comfortable with domain modelling? Try **Project 2** for graph data and compliance logic
- Want to modernise a legacy system using AI assistance? Choose **Project 3**

---

## Getting Started

1. **Choose your project** from the table above
2. **Review the mock data schema** for your chosen project (defined in each project section below)
3. **Choose your path:**
   - **Path A — spec-kit workflow**: Streamlined development using pre-defined prompt files
   - **Path B — manual multi-agent workflow**: Deeper understanding through direct agent collaboration

---

## Project 1 — Transaction Risk Alert Dashboard

### Objective

Build a web application that helps a bank's operations team make rapid risk decisions by visualising real-time transaction risk indicators for customer accounts using mock transaction data served from an in-memory Spring Boot backend.

### Goal

Practice the agentic workflow (Path A or Path B) to develop a complete, functional web application from initial specification through to implementation — all without a real external API.

### Scenario

You are a developer at a fintech company. The fraud operations team needs a tool to assess account risk at a glance. Suspicious transaction activity is rising, and the team wants to see whether an account is **GREEN** (low risk), **YELLOW** (review required), or **RED** (block/escalate). They need to see the key fraud signals driving the score and how transaction behaviour has changed over the past 24 hours.

### Required Features

#### 1. Account Search and Data Retrieval
- Accept input by account ID or customer name
- Fetch mock transaction data from the local Spring Boot mock API
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

Seed data: ~500 transactions across 50 accounts, with 5–10 accounts pre-flagged for high-risk patterns.

---

## Project 2 — AML Pattern Detection Dashboard

### Objective

Build a compliance monitoring dashboard that helps an Anti-Money Laundering (AML) team visualise suspicious transaction chains and account networks, detect known AML typologies, and prioritise which accounts require a Suspicious Activity Report (SAR) or further investigation.

### Goal

Practice the agentic workflow to develop a complete, functional web application. Learn to decompose a complex compliance domain into manageable epics and tasks while maintaining consistency through shared documentation artefacts (`BLUEPRINT.md`, `developer_todo.md`).

### Scenario

You are a developer at a bank's financial crime unit. The compliance team uses vehicle-mounted sensors to... — wait, wrong domain. Here: the compliance team monitors transaction flows across account networks. They need a dashboard to visualise transaction chains in real time, identify AML pattern flags (structuring, layering, round-tripping), and manage a priority queue of accounts for review. The tool should help them decide which accounts need immediate action before reporting deadlines.

### Required Features

#### 1. Transaction Chain Display
- Fetch and display paginated transaction histories for individual accounts
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

Seed data: 50 accounts with relationship edges forming a directed transaction graph; 10 accounts pre-configured with embedded suspicious patterns across all four typologies.

---

## Project 3 — Legacy Banking System Modernisation (Migrate & Modernize Track)

### Objective

Use GitHub Copilot as your migration co-pilot to modernise a provided legacy banking monolith — moving it from an older Java/Spring MVC stack to a production-ready Spring Boot 3.x application, complete with updated data access patterns, a clean REST API, and observability.

### Goal

Practice the agentic workflow in a real-world migration context. Rather than building from scratch, you will analyse existing legacy code with Copilot's help, generate a migration plan, execute incremental refactoring steps, and validate the modernised application — all driven by isolated agent tasks.

### Scenario

You have inherited a legacy account management system built on Spring Framework 4.x and JDBC templates. The system handles core banking operations but is difficult to maintain, lacks observability, and cannot be deployed to a container platform. Your task is to migrate it to Spring Boot 3.x, replace raw JDBC with Spring Data JPA, expose a modern REST API, and add structured logging and health endpoints.

### What Makes This Track Different

- **Migration-first mindset**: You start from existing code, not a blank slate
- **AI-assisted analysis**: Use Copilot to understand legacy patterns before changing them
- **Incremental refactoring**: Each agent task moves one layer forward without breaking the others
- **Validation at every step**: Copilot generates tests before and after each migration step

### Required Deliverables

#### 1. Legacy Code Analysis
- Use Copilot to audit the provided legacy codebase and produce a `migration-plan.md`
- Identify deprecated APIs, JDBC template usages, XML bean definitions, and missing test coverage
- Map existing endpoints and data models to their modern Spring Boot equivalents
- Document breaking changes and migration risks

#### 2. Core Migration
- Replace XML Spring configuration with `@SpringBootApplication` and Java config
- Migrate JDBC templates to Spring Data JPA repositories with H2 for development
- Introduce a layered architecture: Controller → Service → Repository
- Expose the migrated functionality as a documented REST API (Spring Web)
- Add Actuator health and info endpoints

#### 3. Modernisation & Observability
- Add structured JSON logging with contextual MDC fields (account ID, request ID)
- Add `@ControllerAdvice` global error handling with RFC 7807 problem details
- Write JUnit 5 integration tests covering the migrated endpoints
- Produce a `migration-report.md` summarising what changed, what was removed, and what was improved

### Legacy Codebase Schema (provided as starting point)

```
LegacyAccount (JDBC-mapped)
  account_id      : VARCHAR (primary key)
  customer_name   : VARCHAR
  iban            : VARCHAR
  account_type    : VARCHAR ('PERSONAL', 'BUSINESS')
  balance         : DECIMAL
  opened_date     : DATE
  status          : VARCHAR ('ACTIVE', 'FROZEN', 'CLOSED')

LegacyTransaction (JDBC-mapped)
  transaction_id  : VARCHAR (primary key)
  account_id      : VARCHAR (foreign key)
  amount          : DECIMAL
  currency        : VARCHAR
  description     : VARCHAR
  transaction_date : TIMESTAMP
  status          : VARCHAR ('COMPLETED', 'PENDING', 'FAILED')
```

A skeleton legacy project (Spring 4.x, XML config, raw JDBC) is provided in the `legacy-starter/` folder as your migration baseline.

### Technical Migration Path

| Phase | From (Legacy) | To (Modern) |
|---|---|---|
| Configuration | XML beans (`applicationContext.xml`) | `@SpringBootApplication` + Java config |
| Data access | `JdbcTemplate` row mappers | Spring Data JPA + `@Entity` + `@Repository` |
| REST layer | Spring MVC `@Controller` + JSP | Spring Boot `@RestController` + JSON |
| Error handling | Manual try/catch in controllers | `@ControllerAdvice` + problem details |
| Testing | JUnit 4 + manual wiring | JUnit 5 + `@SpringBootTest` |
| Observability | None | Spring Actuator + structured logging |

---

## Mock Data Generation — Common Strategy (All Projects)

All projects use the same approach to avoid requiring real external APIs:

| Concern | Approach |
|---|---|
| Realistic data values | `java-faker` library for IBANs, names, amounts, merchant names, countries |
| Data loading | `DataInitializer` bean using `@EventListener(ApplicationReadyEvent.class)` |
| Storage | H2 in-memory database with JPA entities — no database setup needed |
| Profile switching | `@Profile("mock")` so real data sources can be plugged in later |
| Embedded patterns | Suspicious patterns, risk spikes, and AML typologies baked into seed data intentionally |

---

## Recommended Java Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.x |
| Data | Spring Data JPA + H2 |
| REST | Spring Web (mock API endpoints) |
| Mock generation | java-faker |
| Testing | JUnit 5 + Mockito |
| Build | Maven (multi-module structure recommended) |

---

## Workshop Flow

| Phase | Activity | Approx. Duration |
|---|---|---|
| 0 | Intro: agentic workflow concepts, Copilot agent modes, workspace setup | 30 min |
| 1 | Project selection + mock data schema walkthrough | 15 min |
| 2 | **Blueprint phase**: Architect agent produces `BLUEPRINT.md` (tech decisions, data model, API design) | 30 min |
| 3 | **Planning phase**: Agent generates `developer_todo.md` with phased task breakdown | 20 min |
| 4 | **Implementation**: thread-isolated agent tasks (mock data → service layer → REST API → frontend) | 90 min |
| 5 | **Test phase**: Test Engineer agent generates JUnit 5 and integration tests | 30 min |
| 6 | Demo + retrospective | 30 min |

---

## Agentic Workflow Reference

Refer to the original [Agents and Prompts guide](https://github.com/EficodeDemoOrg/copilot-advanced-exercise-vaisala/blob/main/Agents%20and%20Prompts.md) for the full breakdown of agent roles, thread isolation protocols, and memory persistence strategies. The same workflow applies here — only the domain has changed.

**Key artefacts to produce in every project:**

| File | Produced by | Purpose |
|---|---|---|
| `BLUEPRINT.md` | Architect agent | Technical decisions, data model, API surface, constraints |
| `research.md` | Research agent | Domain notes, compliance rules, data field meanings |
| `developer_todo.md` | Planning agent | Phased task list with acceptance criteria per task |
| `README.md` | Developer agent | Running instructions, architecture summary |
