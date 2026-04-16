package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.AmlFlagRecord;
import com.aml.trustwatch.model.response.ApiResponse;
import com.aml.trustwatch.service.AlertService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getAllAlerts() {
        List<AmlFlagRecord> alerts = alertService.getAllAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @GetMapping("/{flagId}")
    public ResponseEntity<ApiResponse<AmlFlagRecord>> getAlertById(@PathVariable String flagId) {
        AmlFlagRecord alert = alertService.getAlertById(flagId);
        return ResponseEntity.ok(ApiResponse.success(alert));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getAlertsForAccount(@PathVariable String accountId) {
        List<AmlFlagRecord> alerts = alertService.getAlertsForAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @GetMapping("/severity/{severity}")
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getAlertsBySeverity(@PathVariable String severity) {
        List<AmlFlagRecord> alerts = alertService.getAlertsBySeverity(severity);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @GetMapping("/typology/{typology}")
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getAlertsByTypology(@PathVariable String typology) {
        List<AmlFlagRecord> alerts = alertService.getAlertsByTypology(typology);
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @GetMapping("/critical")
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getCriticalAlerts() {
        List<AmlFlagRecord> alerts = alertService.getCriticalAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }

    @GetMapping("/high")
    public ResponseEntity<ApiResponse<List<AmlFlagRecord>>> getHighSeverityAlerts() {
        List<AmlFlagRecord> alerts = alertService.getHighSeverityAlerts();
        return ResponseEntity.ok(ApiResponse.success(alerts));
    }
}

