#!/bin/bash
# gatekeeper.sh — PreToolUse hook
# Reads tool invocation JSON from stdin, writes permissionDecision to stdout.
#
# To register: reference this script in .github/hooks/gatekeeper.json

INPUT=$(cat)
COMMAND=$(echo "$INPUT" | python3 -c "import sys,json; d=json.load(sys.stdin); print(d.get('tool_input',{}).get('command',''))" 2>/dev/null)

# Safe patterns — auto-approve
SAFE_PATTERNS="^(ls|cat|echo|pwd|find|grep|git (log|status|diff|show)|npm (test|run lint)|python -m pytest)"

# Dangerous patterns — force human confirmation  
DANGER_PATTERNS="(rm -rf|drop table|git push --force|git reset --hard|curl|wget|> /)"

if echo "$COMMAND" | grep -qiE "$SAFE_PATTERNS"; then
  echo '{"permissionDecision": "allow"}'
elif echo "$COMMAND" | grep -qiE "$DANGER_PATTERNS"; then
  echo '{"permissionDecision": "ask", "reason": "Destructive or network command requires confirmation"}'
fi
# Anything else: no output — VS Code default approval flow applies
