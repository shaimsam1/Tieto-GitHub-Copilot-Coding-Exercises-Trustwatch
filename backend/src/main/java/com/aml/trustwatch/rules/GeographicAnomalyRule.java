package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Component
public class GeographicAnomalyRule implements Rule {

    private static final String RULE_ID = "GEO_001";
    private static final String RULE_NAME = "Geographic Anomaly Detection";
    private static final Set<String> HIGH_RISK_COUNTRIES = Set.of(
        "VG", "KY", "MT", "CY", "AE", "QA", "LB", "HK"
    );
    private static final int MIN_HIGH_RISK_DESTINATIONS = 2;

    @Override
    public String getRuleId() {
        return RULE_ID;
    }

    @Override
    public String getRuleName() {
        return RULE_NAME;
    }

    @Override
    public Optional<RuleMatch> evaluate(Account account, List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return Optional.empty();
        }

        Set<String> highRiskDestinations = new HashSet<>();

        for (Transaction t : transactions) {
            if (t.getDestinationCountry() != null && HIGH_RISK_COUNTRIES.contains(t.getDestinationCountry())) {
                highRiskDestinations.add(t.getDestinationCountry());
            }
        }

        if (highRiskDestinations.size() >= MIN_HIGH_RISK_DESTINATIONS) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "HIGH",
                String.format("Account %s has transactions to multiple high-risk jurisdictions: %s",
                    account.getAccountId(), highRiskDestinations),
                Instant.now()
            ));
        }

        // Check for rapid cross-border activity
        Set<String> allDestinations = new HashSet<>();
        for (Transaction t : transactions) {
            if (t.getDestinationCountry() != null) {
                allDestinations.add(t.getDestinationCountry());
            }
        }

        if (allDestinations.size() >= 4) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "MEDIUM",
                String.format("Account %s has transactions to %d different countries: %s",
                    account.getAccountId(), allDestinations.size(), allDestinations),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}

