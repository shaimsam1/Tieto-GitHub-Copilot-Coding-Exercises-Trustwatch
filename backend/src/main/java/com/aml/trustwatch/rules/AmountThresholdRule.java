package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class AmountThresholdRule implements Rule {

    private static final String RULE_ID = "AMOUNT_001";
    private static final String RULE_NAME = "Large Transaction Amount";
    private static final BigDecimal HIGH_VALUE_THRESHOLD = new BigDecimal("15000");
    private static final BigDecimal CRITICAL_VALUE_THRESHOLD = new BigDecimal("50000");

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

        Optional<Transaction> criticalTransaction = transactions.stream()
            .filter(t -> t.getAmount() != null)
            .filter(t -> t.getAmount().compareTo(CRITICAL_VALUE_THRESHOLD) >= 0)
            .findFirst();

        if (criticalTransaction.isPresent()) {
            Transaction t = criticalTransaction.get();
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "CRITICAL",
                String.format("Transaction %s has amount %s %s exceeding critical threshold of €50,000",
                    t.getTransactionId(), t.getAmount(), t.getCurrency()),
                Instant.now()
            ));
        }

        Optional<Transaction> highValueTransaction = transactions.stream()
            .filter(t -> t.getAmount() != null)
            .filter(t -> t.getAmount().compareTo(HIGH_VALUE_THRESHOLD) >= 0)
            .filter(t -> t.getAmount().compareTo(CRITICAL_VALUE_THRESHOLD) < 0)
            .findFirst();

        if (highValueTransaction.isPresent()) {
            Transaction t = highValueTransaction.get();
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "HIGH",
                String.format("Transaction %s has amount %s %s exceeding high-value threshold of €15,000",
                    t.getTransactionId(), t.getAmount(), t.getCurrency()),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}

