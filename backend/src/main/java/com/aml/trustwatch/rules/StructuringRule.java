package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class StructuringRule implements Rule {

    private static final String RULE_ID = "STRUCT_001";
    private static final String RULE_NAME = "Potential Structuring";
    private static final BigDecimal REPORTING_THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal STRUCTURING_LOWER_BOUND = new BigDecimal("9000");
    private static final int MIN_SUSPICIOUS_TRANSACTIONS = 3;

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

        Instant twentyFourHoursAgo = Instant.now().minus(Duration.ofHours(24));

        long suspiciousCount = transactions.stream()
            .filter(t -> t.getTimestamp() != null && t.getTimestamp().isAfter(twentyFourHoursAgo))
            .filter(t -> t.getAmount() != null)
            .filter(t -> t.getAmount().compareTo(STRUCTURING_LOWER_BOUND) >= 0)
            .filter(t -> t.getAmount().compareTo(REPORTING_THRESHOLD) < 0)
            .count();

        if (suspiciousCount >= MIN_SUSPICIOUS_TRANSACTIONS) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "CRITICAL",
                String.format("Account %s has %d transactions between €9,000 and €10,000 in 24h (potential structuring)",
                    account.getAccountId(), suspiciousCount),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}

