---
name: project-metrics
description: Collects deterministic project state — file counts by layer, test count, lint status. Invoke at the start of every PM planning session.
---

## Instructions
Run the script below. Do not scan files manually — use only this script's output.
Present the results as a summary table before making any planning decisions.

## Script
```bash
.github/skills/project-metrics/metrics.sh
```

<!-- Create metrics.sh to output:
- Source file count per layer (data, api, logic, ui, tests)
- Total test count
- Lint/build status (pass/fail)
-->
