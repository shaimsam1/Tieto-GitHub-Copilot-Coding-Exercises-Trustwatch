package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.AmlFlagLoader;
import com.aml.trustwatch.model.AmlFlagRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlertServiceTest {

    @Mock
    private AmlFlagLoader amlFlagLoader;

    private AlertService alertService;

    @BeforeEach
    void setUp() {
        alertService = new AlertService(amlFlagLoader);
    }

    @Test
    void shouldReturnAllAlerts() {
        List<AmlFlagRecord> expectedAlerts = List.of(createTestAlert("FLAG-001"));
        when(amlFlagLoader.getAllFlags()).thenReturn(expectedAlerts);

        List<AmlFlagRecord> result = alertService.getAllAlerts();

        assertThat(result).isEqualTo(expectedAlerts);
    }

    @Test
    void shouldReturnAlertById() {
        AmlFlagRecord expectedAlert = createTestAlert("FLAG-001");
        when(amlFlagLoader.getFlagById("FLAG-001")).thenReturn(Optional.of(expectedAlert));

        AmlFlagRecord result = alertService.getAlertById("FLAG-001");

        assertThat(result).isEqualTo(expectedAlert);
    }

    @Test
    void shouldThrowExceptionWhenAlertNotFound() {
        when(amlFlagLoader.getFlagById("FLAG-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> alertService.getAlertById("FLAG-999"))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("FLAG-999");
    }

    @Test
    void shouldReturnAlertsBySeverity() {
        List<AmlFlagRecord> expectedAlerts = List.of(createTestAlert("FLAG-001"));
        when(amlFlagLoader.getFlagsBySeverity("CRITICAL")).thenReturn(expectedAlerts);

        List<AmlFlagRecord> result = alertService.getAlertsBySeverity("CRITICAL");

        assertThat(result).isEqualTo(expectedAlerts);
    }

    @Test
    void shouldReturnCriticalAlerts() {
        List<AmlFlagRecord> expectedAlerts = List.of(createTestAlert("FLAG-001"));
        when(amlFlagLoader.getFlagsBySeverity("CRITICAL")).thenReturn(expectedAlerts);

        List<AmlFlagRecord> result = alertService.getCriticalAlerts();

        assertThat(result).isEqualTo(expectedAlerts);
    }

    private AmlFlagRecord createTestAlert(String id) {
        AmlFlagRecord alert = new AmlFlagRecord();
        alert.setFlagId(id);
        alert.setAccountId("ACC-001");
        alert.setTypology("STRUCTURING");
        alert.setConfidenceScore(85);
        alert.setSeverity("CRITICAL");
        alert.setDetectedAt(Instant.now());
        alert.setRecommendedAction("SAR");
        return alert;
    }
}

