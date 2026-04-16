package com.aml.trustwatch.model;

import java.time.Instant;

public class RuleMatch {

    private String ruleId;
    private String ruleName;
    private String severity;
    private String description;
    private Instant detectedAt;

    public RuleMatch() {
    }

    public RuleMatch(String ruleId, String ruleName, String severity, String description, Instant detectedAt) {
        this.ruleId = ruleId;
        this.ruleName = ruleName;
        this.severity = severity;
        this.description = description;
        this.detectedAt = detectedAt;
    }

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }
}

