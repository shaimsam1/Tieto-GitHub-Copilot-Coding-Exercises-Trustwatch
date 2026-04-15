---
name: Agent Name
description: One-line description of what this agent does and nothing else.
tools: [read/readFile, write/writeFile, search/codebase]
model: GPT-5.2 (copilot)
---

## Responsibility
<!-- Single sentence: the one thing this agent is responsible for. -->

## Boundaries
This agent will NOT:
- Modify files outside `<domain>/`
- Implement code (planning only / or vice versa)
- 

## Protocol
<!-- How it produces output: format, required review steps, human checkpoints. -->
1. 
2. Stop and report back before proceeding past [checkpoint].

## References
- Domain instructions: `.github/instructions/<domain>.instructions.md`
