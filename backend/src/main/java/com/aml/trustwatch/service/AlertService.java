package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.AmlFlagLoader;
import com.aml.trustwatch.model.AmlFlagRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertService {

    private final AmlFlagLoader amlFlagLoader;

    public AlertService(AmlFlagLoader amlFlagLoader) {
        this.amlFlagLoader = amlFlagLoader;
    }

    public List<AmlFlagRecord> getAllAlerts() {
        return amlFlagLoader.getAllFlags();
    }

    public AmlFlagRecord getAlertById(String flagId) {
        return amlFlagLoader.getFlagById(flagId)
            .orElseThrow(() -> new ResourceNotFoundException("Alert", flagId));
    }

    public List<AmlFlagRecord> getAlertsForAccount(String accountId) {
        return amlFlagLoader.getFlagsForAccount(accountId);
    }

    public List<AmlFlagRecord> getAlertsBySeverity(String severity) {
        return amlFlagLoader.getFlagsBySeverity(severity);
    }

    public List<AmlFlagRecord> getAlertsByTypology(String typology) {
        return amlFlagLoader.getFlagsByTypology(typology);
    }

    public List<AmlFlagRecord> getCriticalAlerts() {
        return amlFlagLoader.getFlagsBySeverity("CRITICAL");
    }

    public List<AmlFlagRecord> getHighSeverityAlerts() {
        return amlFlagLoader.getFlagsBySeverity("HIGH");
    }
}

