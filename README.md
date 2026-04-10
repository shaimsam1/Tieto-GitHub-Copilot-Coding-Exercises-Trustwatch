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
| **Name** | Transaction Risk Alert Dashboard | AML Pattern Detection Dashboard | AI-Powered Financial Intelligence Assistant |
| **Complexity** | Beginner–Intermediate | Intermediate–Advanced | Advanced |
| **Data Source** | Mock REST API (Spring Boot) | Mock REST API (Spring Boot) | Mock REST API (two endpoints) |
| **Data Focus** | Per-account transaction risk | Account network AML patterns | Portfolio risk + market intelligence |
| **AI Integration** | Display only | Display + rule-based analysis | AI reasoning + decision support |
| **Best For** | Learning the agentic workflow basics | Complex domain modelling and graph data | AI integration and multi-source reasoning |

**Recommendation:**
- New to agentic workflows? Start with **Project 1**
- Comfortable with domain modelling? Try **Project 2** for graph data and compliance logic
- Want to explore AI reasoning and multi-source data? Choose **Project 3**

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

## Project 3 — AI-Powered Financial Intelligence Assistant (Advanced)

### Objective

Build an intelligent conversational assistant that combines a mock market intelligence endpoint with internal portfolio and transaction risk data — both served from a local Spring Boot backend — to provide comprehensive financial decision support through natural language interaction.

### Goal

Practice the agentic workflow while integrating AI capabilities through tool-calling. Learn to combine multiple data sources — forward-looking market intelligence and backward-looking portfolio observations — and enable natural language interaction for complex financial reasoning scenarios.

### Scenario

You are building an AI-powered assistant for relationship managers and risk analysts at a regional bank. The assistant must answer complex, multi-source questions such as:

- *"Should we increase credit exposure to the Retail sector this quarter?"*
- *"Which client portfolios are most exposed to current FX volatility?"*
- *"Flag any accounts with overlapping AML signals and deteriorating sector fundamentals."*

Answering these questions requires reasoning across **market intelligence** (external, predictive) and **internal portfolio and transaction data** (mock, observational). The assistant should explain its reasoning and cite its sources.

### What Makes This Project Advanced

- **Dual Mock API Integration**: Fetch data from two separate mock endpoints — a market intelligence feed and an internal portfolio/transaction API — both served from the local Spring Boot backend
- **AI Tool-Calling**: Register both endpoints as tools so the AI model can invoke them during a conversation turn
- **AI Reasoning Layer**: The AI actively reasons across both sources rather than simply displaying data
- **Multi-Source Decision Making**: Correlate market signals with internal portfolio observations

Example reasoning chains:
- *"Retail sector credit risk is elevated (market signal) + three clients show increasing transaction anomalies (internal data) = recommend tightening credit limits"*
- *"FX volatility spike in EUR/USD (market) + portfolio has 40% EUR-denominated holdings (internal) = flag for hedging review"*
- *"SAR filed on account network (internal) + sanctions screening list updated (external) = escalate to compliance immediately"*

### Required Features

#### 1. Market Intelligence Integration (Mock Endpoint)
- Fetch mock market signals from the local Spring Boot backend under `/market/signals`
- Enable natural language queries routed to the AI model with the fetched market context injected as tool output
- Support queries such as:
  - *"What is the current credit risk outlook for the Financial Services sector?"*
  - *"Summarise recent FX movements affecting EUR-denominated portfolios."*
  - *"Are there any active sanctions or watchlist updates relevant to Eastern European entities?"*
- Display the AI response in a readable format with the data source cited

#### 2. Internal Portfolio Data Integration
- Fetch mock portfolio and transaction risk data from the local Spring Boot API
- Display alongside market intelligence in a unified view
- Show correlations between external signals and internal observations:
  - Sector risk elevated externally + client portfolio concentrated in that sector
  - FX movement + currency exposure breakdown of holdings
  - AML flag raised internally + relevant external sanctions or typology intelligence
- Use colour-coding to highlight critical combinations

#### 3. Conversational Decision Support
- Build a chat-style UI for natural language interaction
- Enable complex multi-source questions to be answered in a single conversation turn
- Display the AI's reasoning process:
  - Which data sources were accessed
  - What inferences were drawn
  - What the confidence level is
  - What action is recommended and why
- Include source citations distinguishing market intelligence (external) from portfolio data (internal mock API)

### Mock Data Schema

```
Portfolio
  portfolioId      : UUID
  clientName       : String
  relationshipManager : String
  totalValueEur    : BigDecimal
  riskProfile      : Enum (CONSERVATIVE, BALANCED, AGGRESSIVE)
  holdings         : List<Holding>

Holding
  holdingId        : UUID
  assetClass       : Enum (EQUITY, FIXED_INCOME, FX, COMMODITY, ALTERNATIVE)
  sector           : String (e.g., RETAIL, ENERGY, FINANCIAL_SERVICES)
  currency         : String (ISO 4217)
  valueEur         : BigDecimal
  exposurePct      : Double
  riskScore        : Integer (0–100)

MarketSignal (served from mock market endpoint `/market/signals`)
  signalId         : UUID
  category         : Enum (SECTOR_RISK, FX_VOLATILITY, SANCTIONS_UPDATE, CREDIT_RATING_CHANGE)
  description      : String
  severity         : Enum (INFO, WATCH, WARNING, CRITICAL)
  affectedSectors  : List<String>
  affectedCurrencies : List<String>
  publishedAt      : Instant
  source           : String
```

Seed data: 20 portfolios with varied holdings, 10 market signal events (3 critical, 4 warning, 3 info) designed to create interesting cross-source correlations with the portfolio data.

### Technical Architecture

1. **Market Intelligence Mock API** (Spring Boot)
   - Mock REST endpoint under `/market/signals` serving `MarketSignal` records
   - Additional endpoints: `/market/sector-outlook`, `/market/fx-summary`
   - Registered as an AI tool so the model can call it during a conversation turn

2. **Internal Portfolio Mock API** (Spring Boot)
   - `/portfolios` — list all portfolios
   - `/portfolios/{id}` — portfolio detail with holdings
   - `/portfolios/{id}/risk` — current risk assessment
   - `/transactions/{accountId}` — transaction history for a client

3. **AI Orchestration**
   - Spring AI or LangChain4j for tool-calling and prompt orchestration
   - System prompt defines the assistant's role and reasoning protocol
   - Tool definitions expose both the market intelligence endpoint and the portfolio API to the model
   - No external API keys or additional servers required — all data served locally from Spring Boot

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
| AI integration (Project 3) | Spring AI or LangChain4j |
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
