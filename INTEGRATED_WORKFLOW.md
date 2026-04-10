# Agent-Driven Development — Integrated Workflow Guide

A step-by-step guide for building a complete project using an agentic workflow. Technology-agnostic. Applies to any project scope or complexity level.

---

## Two Principles to Carry Through Every Phase

1. **Human-in-the-loop.** Design your agents to stop and report back at key decision points. A human reviews, comments, and approves before work continues. An agent that runs end-to-end without checkpoints is a liability, not an asset.
2. **Brevity.** Agent definitions, skill instructions, and protocols that grow beyond ~150 lines lose focus. Keep instructions tight, specific, and structured.

---

## Design Checklist

Review this before you consider any phase complete:

- [ ] Does each agent stop for human approval at key decision points?
- [ ] Could any LLM guesswork be replaced with a deterministic tool (script, skill, hook) to get facts into context?
- [ ] Are agent instructions under ~150 lines and clearly structured?
- [ ] Is each agent's tool list minimal — only what it actually needs?
- [ ] Does each agent have a defined input/output contract?
- [ ] Have you tested each agent with a concrete task and observed its behaviour?

---

## Phase 0 — Environment & Project Foundation

### 0.1 Select Your Project

Choose what you want to build. Consider:
- What problem does it solve?
- What are the main functional areas (data ingestion, display, analysis, interaction)?
- What is the expected complexity? Start simpler if new to agentic workflows.

### 0.2 Verify Your Environment

Before anything else, confirm:
- Your IDE and Copilot extensions are installed and functional
- Agent mode is available and responsive
- You can switch between custom agents in the agent picker

### 0.3 Enable the Chat Debug View

Open your IDE's Chat panel, access the `...` menu, and select **Open Chat Debug View**. This reveals the agent's reasoning blocks, tool invocations, and skill/hook execution logs. Use it throughout every phase to understand *why* an agent made a decision.

### 0.4 Create the Infrastructure Directories

```
.github/agents/
.github/skills/
.github/hooks/
.github/instructions/
.github/prompts/
```

These directories house every artefact you build. Nothing lives in ad-hoc locations.

---

## Phase 1 — Blueprint & Research

*Produce the foundational artefacts before writing a single line of implementation code.*

### 1.1 Define the Project with AI (Ask Mode)

Open Ask mode. Use it as a sparring partner — not a code generator. Ask it to quiz you about:
- The problem your project solves and who it serves
- The key features and how users will interact with them
- The data your project needs to work with
- The architectural layers (data layer, logic layer, presentation layer)
- Constraints and non-functional requirements

Let the AI ask clarifying questions. The goal is to surface decisions you haven't made yet.

### 1.2 Generate `BLUEPRINT.md`

Have the AI produce a project specification covering:
- **Purpose and user scenarios** — who uses it, what they do, what they need to see
- **Feature list** — broken down by functional area
- **Data model** — the key entities, their fields, and relationships
- **API surface** — the endpoints or interfaces that will expose data
- **Constraints** — anything that must remain true (no external APIs, in-memory data, mock endpoints, etc.)

This is the single source of truth all subsequent agents reference.

### 1.3 Generate `research.md`

For domain-heavy projects, produce a research document covering:
- Key domain concepts (what does each term mean in this context?)
- Rules and thresholds embedded in the domain (e.g., what makes something "high risk"?)
- Data field meanings and acceptable values
- Edge cases and known data quality issues

This ensures agents have domain knowledge without you re-explaining it every thread.

### 1.4 Generate `developer_todo.md`

Have the AI produce a phased task breakdown. Every task must:
- Be completable in a **single agent context window** (a rough guide: ≤5 files touched)
- Have a clear **acceptance criterion** — a specific, testable statement of done
- Include a **TDD requirement** — what tests should exist before the task is considered complete
- Be tagged with the domain it belongs to (data layer, API layer, frontend, etc.)

Tasks that are too large must be split. An agent that receives an oversized task will lose context mid-way.

**Checkpoint:** Do not proceed to Phase 2 until `BLUEPRINT.md`, `research.md`, and `developer_todo.md` all exist and have been reviewed.

---

## Phase 2 — Base & Domain Instructions

*Encoding your standards so every agent follows them without being told repeatedly.*

### 2.1 Generate Base Custom Instructions

Create `.github/copilot-instructions.md`. This file applies universally — it sets project-wide conventions. Use Ask mode (with `BLUEPRINT.md` as context) to generate it. Include:
- Project purpose in one paragraph
- Code style conventions (naming, file organisation)
- Universal error handling expectations
- Commit and documentation expectations

These are the non-negotiables. Do not include domain-specific rules here.

### 2.2 Identify Your Domains

With the AI, analyse your project structure and identify distinct domains. Common examples:
- **Data layer** — persistence, seeding, schema
- **API layer** — request handling, response formatting, validation
- **Business logic** — rules, scoring, pattern detection
- **Presentation layer** — components, charts, state
- **Tests** — unit and integration test conventions

For each domain, define:
- A glob pattern that matches its files
- The key concerns specific to that domain (security, performance, patterns to use or avoid)

Document this in a `DOMAIN_STRUCTURE.md` file.

### 2.3 Generate Domain Instruction Files

For each domain, create a file in `.github/instructions/` named `<domain>.instructions.md`. Include in the YAML frontmatter an `applyTo` glob pattern that activates it automatically when matching files are open.

Use the AI to generate each file. For each domain, research the best practices relevant to it before generating — ask the AI what the industry-standard concerns are for that domain in your chosen stack.

Each instruction file should cover:
- What to always do (patterns, validations, structure)
- What to never do (anti-patterns, shortcuts that cause problems)
- Domain-specific security or performance concerns

Do not duplicate guidance already in the base instructions.

### 2.4 Validate for Contradictions

Open a fresh Agent mode session. Add all instruction files as context. Ask the AI:
- Are there any contradictions between domain instructions and base instructions?
- Are there gaps — important standards not covered anywhere?
- Are any instructions ambiguous or likely to be interpreted inconsistently?

Resolve every issue raised before moving on.

---

## Phase 3 — Specialist Agents

*Creating focused AI assistants with clear roles and enforced boundaries.*

### 3.1 Design Your Specialists with AI

Use Ask mode with `DOMAIN_STRUCTURE.md` as context. For each domain, design a specialist agent:
- What is its **single responsibility**?
- What **tools** does it need? (file editing, terminal, search, problems — only what it genuinely needs)
- What are its **explicit boundaries**? What files or domains must it never touch?
- What **protocols** ensure consistent output?

Document the design before creating any files.

### 3.2 Create Specialist Agent Files

Create one `.agent.md` file per specialist in `.github/agents/`. Each file has YAML frontmatter defining the agent's name, description, allowed tools, and optionally a preferred model.

Use Agent mode to scaffold these files — provide the design from 3.1 and ask it to generate the `.agent.md` frontmatter and instructions. Review and refine the output; do not hand-author YAML by guessing.

Each agent's instructions must include:
- A clear statement of its responsibility
- An explicit list of what it will not do
- A reference to the relevant domain instruction file
- A protocol for how it produces output (what format, what review steps)

### 3.3 Create the Project Manager Agent

The PM agent is distinct from domain specialists. It:
- Analyses the current state of the project
- Selects and elaborates backlog items from `developer_todo.md`
- Produces structured, properly sized backlog items with acceptance criteria and TDD requirements
- Does **not** implement anything

The PM should reference the `create-backlog-item` skill (which you will build in Phase 4) in its instructions so it always uses a consistent item structure.

### 3.4 Validate Agent Boundaries

Test each specialist:
- Ask a domain question it should answer — verify it answers correctly
- Ask a question that falls outside its domain — verify it declines and suggests the correct agent
- Switch between specialists for a feature that spans two domains — verify each maintains focus

---

## Phase 4 — Skills

*Replacing LLM guesswork with deterministic, reusable capabilities.*

### 4.1 Understand the Distinction

A skill is not a custom instruction. It is a directory under `.github/skills/<name>/` containing:
- A `SKILL.md` with a `name` and `description` in its YAML frontmatter
- Optional scripts that run deterministically
- Optional resource files (templates, reference data) the agent loads verbatim

Skills are loaded on-demand when the agent judges them relevant. They are portable across agents. Use them when you want the agent to have a specific, repeatable capability — not just general guidance.

**Rule of thumb:** If an LLM might guess wrong, make it a script. If the value is in the structure (not the execution), make it a resource template.

### 4.2 Create `create-backlog-item`

This skill ensures every backlog item has the same structure regardless of which agent creates it.

Create `.github/skills/create-backlog-item/` with:
- `SKILL.md` — instructs the agent to always use the included template
- `backlog-item-template.md` — the canonical item structure: title, description, acceptance criteria, TDD plan, file-scope estimate, definition of done

Reference this skill explicitly in the PM agent's instructions.

### 4.3 Create `run-tests`

This skill ensures agents run tests correctly — with the right command, the right flags, and structured output.

Without it, agents invent their own test command, which is often wrong.

Create `.github/skills/run-tests/` with:
- `SKILL.md` — instructions for test execution
- A shell script that runs the test suite and outputs structured pass/fail information (test count, failing test names, exit code)

This skill is reusable. Any agent that needs to verify tests can invoke it.

### 4.4 Create `project-metrics`

This skill gives the PM agent deterministic project state so it does not guess.

Create a script that collects: source file count by layer, test count, any lint or build status signals. The PM uses this before planning instead of scanning files manually.

### 4.5 Additional Skills

Think about what your agents do repeatedly and where a script would produce better results than the model guessing. For each candidate skill:
- Create the directory structure
- Write scripts that produce structured, parseable output
- Keep each skill focused — a skill that tries to do too much loses effectiveness
- Control visibility with `user-invocable` and `disable-model-invocation` frontmatter properties

### 4.6 Create Workflow Prompt Files

In `.github/prompts/`, create reusable prompt files that automate common multi-step tasks. Each file has YAML frontmatter specifying the mode and tools, and a body that guides the user through a structured workflow.

Suggested prompts based on your project's required features:
- One for creating a new data-layer component from a schema definition
- One for creating an API endpoint with validation and error handling
- One for creating a presentation-layer component connected to an API endpoint

---

## Phase 5 — Hooks

*System-level guarantees that instructions alone cannot provide.*

### 5.1 Understand the Distinction

An instruction that says "always run the linter" is a suggestion the LLM may ignore. A hook that runs the linter is a guarantee.

Hooks are shell commands that run at specific lifecycle points. They're defined in JSON files in `.github/hooks/`. Use them for guarantees, not guidance.

Available lifecycle events: `SessionStart`, `UserPromptSubmit`, `PreToolUse`, `PostToolUse`, `PreCompact`, `SubagentStart`, `SubagentStop`, `Stop`.

### 5.2 Build the Smart Gatekeeper (`PreToolUse`)

Create a `PreToolUse` hook that inspects every terminal command before it runs:

| Pattern type | Decision | Rationale |
|---|---|---|
| Read-only / safe commands | `allow` — auto-approve | No side effects; safe to run without supervision |
| Destructive / network / irreversible | `ask` — force confirmation | Human must approve before execution |
| Everything else | Pass through | Default VS Code approval flow handles it |

The hook script reads the tool invocation JSON, extracts the command, matches it against allow and deny patterns, and writes a JSON output with the `permissionDecision` field.

**Test it:** Run a safe command (should auto-approve). Run a destructive command (should prompt). Confirm in the Chat Debug View.

### 5.3 Add a Post-Edit Quality Hook (`PostToolUse`)

Create a `PostToolUse` hook that runs your linter or formatter after every file edit. This enforces code quality as a system guarantee, not a suggestion.

### 5.4 Add the PM Audit Hook (Agent-Scoped)

Add an agent-scoped hook to the PM agent's frontmatter. On `Stop`, append a timestamped entry to `.github/pm-audit.log`. This creates a persistent record of PM activity independent of chat history.

Agent-scoped hooks run only when that agent is active. They are defined in the agent's `.agent.md` frontmatter using the `hooks` field.

**Test it:** Run a PM session. Verify the log file updates. Verify the hook appears in the Chat Debug View.

### 5.5 Consider a Test-Gate Hook

A `Stop` hook that runs the full test suite before the agent session ends. If tests fail, the agent session blocks. Note: a `Stop` hook that blocks must check `stop_hook_active` to prevent infinite loops.

---

## Phase 6 — Feature Implementation

*Using the full domain system to build the project's required features.*

### 6.1 Select a Feature

Review `developer_todo.md`. Use the PM agent to:
1. Assess the current project state (using the `project-metrics` skill)
2. Select the next logical backlog item
3. Produce a fully elaborated item using the `create-backlog-item` skill

Review the item. If it's too large (touches >5 files), ask the PM to split it. Approve the item before any implementation begins.

### 6.2 Backend / Data Layer Implementation

Switch to the appropriate specialist agent. Use the relevant workflow prompt. Follow this sequence:
1. Start with the data model and persistence layer
2. Add the API or service layer on top
3. Add validation and error handling
4. Ask the specialist to review the output for standard compliance before moving on

Do not move to the presentation layer until the data and API layers are stable.

### 6.3 Presentation Layer Implementation

Switch to the frontend specialist. Use the workflow prompt for the presentation component. Ensure it:
- Connects to the API layer built in 6.2
- Handles loading states, empty states, and errors
- Meets the acceptance criteria from the backlog item

Ask the specialist to evaluate the component for accessibility and user experience before signing off.

### 6.4 Testing — Strict TDD Loop

Switch to the Test Engineer. The sequence must be:

1. **Write failing tests first** that directly map to the acceptance criteria
2. **Run the tests** using the `run-tests` skill — confirm they fail for the right reasons
3. **Implement** the production code to make the tests pass
4. **Run the tests again** — loop until all pass
5. **Do not declare success** until the `run-tests` skill reports all green

The Test Engineer must not skip to step 3. If it does, redirect it.

### 6.5 Cross-Domain Validation

Return to general Agent mode. Verify that the layers integrate correctly:
- Does the frontend correctly consume the API?
- Do the tests cover the integration points, not just the units?
- Ask for a final review of the complete feature for security, performance, and standards compliance

### 6.6 Model Selection

Choose the right model for each task:
- **Architectural and multi-file reasoning, security review** → highest-capability model (e.g., Claude Sonnet)
- **Boilerplate generation, simple refactoring, documentation** → faster/cheaper model
- Check the model comparison reference for your subscription tier and adjust accordingly

---

## Phase 7 — Orchestration

*Automating the coordination that was manual in Phase 6.*

### 7.1 Why Orchestrate

In Phase 6 you manually: switched between agents, copy-pasted plans, re-explained context. Orchestration automates exactly that. The key mechanism is the **context window economy**:

| Role | Context profile | Purpose |
|---|---|---|
| **Researcher** | Reads many files, produces a compact summary | Disposable context — run once, summarise, discard |
| **Coordinator** | Sees only summaries, makes decisions | Must stay "blind" to file contents — only summaries |
| **Implementer** | Receives precise instructions, writes code | No re-discovery needed |

Each agent runs in its own window. Separating roles keeps each window lean.

### 7.2 Design the Workflow

Before building anything, design the orchestration as a group. For each agent in the workflow, define:
- **Input contract** — what structured input it receives
- **Output contract** — what structured output it produces
- **Tools** — only what it needs for its role
- **Human checkpoints** — where it must stop and wait for approval

Design the orchestration diagram first. Validate it with the teacher agent if unsure.

### 7.3 Convert Existing Agents to Subagents

- Set `user-invocable: false` on the PM and Implementer
- Add an `Input/Output Contract` section to each agent's instructions
- Review their tools lists — subagents often need fewer tools than human-invocable agents

### 7.4 Create the Coordinator Agent

The Coordinator:
- Has `tools: [agent]` only — **no file editing, no file reading**
- Delegates all file reading to Researcher subagents
- Delegates all file writing to worker subagents
- Defines the full workflow sequence in its instructions
- Stops for human approval after research and before implementation

Use the `agents` frontmatter property to restrict which subagents it can invoke.

### 7.5 Create the Researcher Agent

The Researcher:
- Has read-only tools
- Receives a specific question or scope from the Coordinator
- Returns a compact, structured summary — never raw file contents
- Its context is disposable — it reads everything it needs, summarises, and terminates

### 7.6 Run a Feature End-to-End

Give the Coordinator a feature request taken directly from your project's required features list. Observe:
- Does the Coordinator delegate, or does it try to do everything itself?
- Does it stop at the designated human checkpoints?
- Are research summaries concise enough, or do they bloat the Coordinator's context?
- Does the TDD cycle execute correctly in the subagent context?
- Do skills (create-backlog-item, run-tests) and hooks (Smart Gatekeeper, post-edit linting) still fire in subagent sessions?

**Iterate on agent instructions based on what you observe.** The first version will not be perfect.

### 7.7 Add the PM → Coordinator Handoff (Stretch)

Add a `handoffs` property to the PM agent's frontmatter pointing to the Coordinator. Update the PM's instructions: after producing a backlog item, it offers the user a handoff. The Coordinator receives the conversation and kicks off the implementation workflow.

Test: ask the PM to propose a feature. It should offer the handoff. Accept it. Observe the Coordinator taking over.

---

## Artefact Reference

| File | Produced by | Purpose |
|---|---|---|
| `BLUEPRINT.md` | Architect (Ask mode) | Technical decisions, data model, API surface, constraints |
| `research.md` | Research (Ask mode) | Domain notes, rules, field meanings, edge cases |
| `developer_todo.md` | Planning (Ask mode) | Phased task list with acceptance criteria and TDD requirements |
| `DOMAIN_STRUCTURE.md` | Ask mode | Domains, glob patterns, key concerns per domain |
| `.github/copilot-instructions.md` | Agent mode | Universal project conventions |
| `.github/instructions/<domain>.instructions.md` | Agent mode | Domain-specific standards, activated by `applyTo` glob |
| `.github/agents/<name>.agent.md` | Agent mode | Specialist agent definition (tools, boundaries, protocols) |
| `.github/skills/<name>/SKILL.md` | Agent mode | Reusable skill with optional scripts and resource files |
| `.github/prompts/<name>.prompt.md` | Agent mode | Reusable workflow automation prompts |
| `.github/hooks/<name>.json` | Agent mode | Lifecycle hook definitions |
| `.github/pm-audit.log` | PM audit hook | Timestamped record of PM agent sessions |

---

## Debugging Guide

| Symptom | Where to look | What to check |
|---|---|---|
| Agent ignores instructions | Chat Debug View → context panel | Is the instruction file in context? Does the `applyTo` glob match? |
| Skill never fires | Chat Debug View → skill log | Is the skill name in the agent's `agents` list or instructions? |
| Hook doesn't run | Chat Debug View → execution log | Is the hook JSON valid? Is the event name correct? |
| Agent crosses domain boundary | Agent instructions | Is the boundary stated explicitly as a prohibition? |
| Coordinator reads files itself | Coordinator tools list | Remove file-reading tools; delegate via Researcher |
| TDD loop skipped | Implementer instructions | State the sequence explicitly: tests first, fail confirmation, then implementation |
| Subagent ignores I/O contract | Subagent instructions | Add a dedicated `Input/Output Contract` section |

---

## Open Questions to Discuss at Each Phase

- How specific should instructions be vs. how much should the model exercise judgment?
- Which tasks genuinely need the most capable model, and which can use a faster one?
- Where are the right human checkpoints — frequent enough to catch errors, infrequent enough not to defeat the purpose of automation?
- Which skills are reusable across multiple agents, and which are agent-specific?
- What happens when an agent fails mid-task? How does the Coordinator recover?
