---
name: run-tests
description: Runs the test suite with the correct command and flags. Returns structured pass/fail output. Invoke before declaring any task complete.
---

## Instructions
Run the test script below. Do not invent a test command.
Report: total tests, passing count, failing test names, exit code.
If exit code is non-zero, do not proceed — report failures to the user.

## Script
```bash
.github/skills/run-tests/run-tests.sh
```

<!-- Create run-tests.sh with the project's actual test command, e.g.:
#!/bin/bash
set -e
npm test --reporter=json 2>&1
-->
