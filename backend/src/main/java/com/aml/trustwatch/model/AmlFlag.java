package com.aml.trustwatch.model;

import java.time.Instant;

public class AmlFlag {
    
    private String flagId;
    private String typology;
    private int confidenceScore;
    private String severity;
    private Instant detectedAt;
    private String recommendedAction;

    public AmlFlag() {
    }

    public AmlFlag(String flagId, String typology, int confidenceScore, String severity,
                   Instant detectedAt, String recommendedAction) {
        this.flagId = flagId;
        this.typology = typology;
        this.confidenceScore = confidenceScore;
        this.severity = severity;
        this.detectedAt = detectedAt;
        this.recommendedAction = recommendedAction;
    }

    public String getFlagId() {
        return flagId;
    }

    public void setFlagId(String flagId) {
        this.flagId = flagId;
    }

    public String getTypology() {
        return typology;
    }

    public void setTypology(String typology) {
        this.typology = typology;
    }

    public int getConfidenceScore() {
        return confidenceScore;
    }

    public void setConfidenceScore(int confidenceScore) {
        this.confidenceScore = confidenceScore;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Instant getDetectedAt() {
        return detectedAt;
    }

    public void setDetectedAt(Instant detectedAt) {
        this.detectedAt = detectedAt;
    }

    public String getRecommendedAction() {
        return recommendedAction;
    }

    public void setRecommendedAction(String recommendedAction) {
        this.recommendedAction = recommendedAction;
    }
}

