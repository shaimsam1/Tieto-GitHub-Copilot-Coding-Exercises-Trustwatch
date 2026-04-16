# Plan: AML Pattern Detection Dashboard — Copilot Instructions File

## Goal
Create `.github/copilot-instructions.md` — a comprehensive workspace instruction file that guides GitHub Copilot when generating code for an AML Pattern Detection Dashboard (Angular UI + Java backend, JSON data source, rule-based AI analysis, no graph/chart library).

---

## Steps

### Phase 1 — File Scaffold
1. Create `.github/copilot-instructions.md` with all section headings.

### Phase 2 — Section Authoring *(sections are independent)*

2. **Project Overview** — purpose of the dashboard, AML domain summary (account networks, suspicious transaction patterns), high-level text architecture.

3. **Tech Stack & Constraints**
   - Angular 17+ (standalone components)
   - Java 21 + Spring Boot 3
   - Static JSON files as the sole data source (no DB, no ORM, no Spring Data, no graph lib)
   - REST JSON API between frontend and backend

4. **Angular Component & Module Structure**
   - Feature module layout: `aml-dashboard/`, `pattern-viewer/`, `account-network/`
   - Smart vs. dumb component split
   - Service conventions, routing conventions
   - Plain HTML/CSS tables and card-based rendering for account network display (no external chart/graph library)

5. **Java Service & REST API Conventions**
   - Package structure: `controller/`, `service/`, `model/`, `loader/`
   - `@RestController` + `@Service` naming patterns
   - Load and cache static JSON files using `ClassPathResource` + Jackson `ObjectMapper`
   - Standard REST response envelope: `{ data, meta, errors }`

6. **Static JSON Fixture Schema**
   - Canonical fixture files under `src/main/resources/data/`:
     - `accounts.json`
     - `transactions.json`
     - `relationships.json`
     - `aml-alerts.json`
   - Defines field names, types, and inter-file relationships (account → transactions → linked accounts)
   - Copilot must always read from these files only — never from a database

7. **Rule-Based AI Analysis Guidelines**
   - Deterministic rule functions: velocity checks, circular-flow detection, structuring threshold rules
   - Location: `RuleEngine.java` + `rules/` package
   - API response includes `ruleMatches[]` array with matched rule details
   - Angular UI renders rule match badges/highlights using Angular directives only

8. **Code Style**
   - Java: Google Java Style, no Lombok, explicit constructors
   - Angular: ESLint + Prettier, `camelCase` services, `kebab-case` selectors
   - JSON: `camelCase` keys, ISO-8601 dates

### Phase 3 — Inline Verification Hints
9. Add a "Verification" note block at the bottom of the instruction file:
   - `ng build --configuration production`
   - `mvn clean verify`
   - Sample `curl` call to confirm the JSON loader endpoint responds correctly

---

## Relevant Files
- `.github/copilot-instructions.md` — file to create (does not exist yet)
- `src/main/resources/data/*.json` — static JSON fixtures (data source)

---

## Verification (After Implementation)
1. Open a new Agent mode chat, type `@workspace` — confirm instructions auto-load via "Used X instructions" indicator.
2. Prompt Copilot to generate `AccountService.java` — verify it uses `ClassPathResource` + `ObjectMapper`, not a DB.
3. Prompt Copilot to scaffold an Angular pattern-viewer component — verify it follows module/selector conventions and uses no graph or chart library.

---

## Decisions
- **Scope**: `.github/copilot-instructions.md`, workspace-level, team-shared
- **Data source**: Static JSON files only — no database, no ORM, no Spring Data
- **UI rendering**: Plain Angular HTML/CSS tables and cards — no graph/chart library
- **AI integration**: Rule-based only; UI displays rule match results via Angular directives
- **Excluded**: AML pattern catalogue, graph model conventions, graph/chart visualization library

---

*Created: 2026-04-16*
