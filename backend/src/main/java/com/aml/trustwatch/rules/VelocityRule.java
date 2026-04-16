package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class VelocityRule implements Rule {

    private static final String RULE_ID = "VELOCITY_001";
    private static final String RULE_NAME = "High Velocity Transactions";
    private static final int MAX_TRANSACTIONS_PER_HOUR = 5;
    private static final int MAX_TRANSACTIONS_PER_DAY = 15;

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

        Instant now = Instant.now();
        Instant oneHourAgo = now.minus(Duration.ofHours(1));
        Instant oneDayAgo = now.minus(Duration.ofDays(1));

        long hourlyCount = transactions.stream()
            .filter(t -> t.getTimestamp() != null && t.getTimestamp().isAfter(oneHourAgo))
            .count();

        long dailyCount = transactions.stream()
            .filter(t -> t.getTimestamp() != null && t.getTimestamp().isAfter(oneDayAgo))
            .count();

        if (hourlyCount > MAX_TRANSACTIONS_PER_HOUR) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "HIGH",
                String.format("Account %s has %d transactions in the last hour (threshold: %d)",
                    account.getAccountId(), hourlyCount, MAX_TRANSACTIONS_PER_HOUR),
                Instant.now()
            ));
        }

        if (dailyCount > MAX_TRANSACTIONS_PER_DAY) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "MEDIUM",
                String.format("Account %s has %d transactions in the last 24 hours (threshold: %d)",
                    account.getAccountId(), dailyCount, MAX_TRANSACTIONS_PER_DAY),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}

