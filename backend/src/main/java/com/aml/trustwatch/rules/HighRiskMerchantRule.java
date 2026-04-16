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
public class HighRiskMerchantRule implements Rule {

    private static final String RULE_ID = "MERCHANT_001";
    private static final String RULE_NAME = "High Risk Merchant Activity";
    private static final Set<String> HIGH_RISK_CATEGORIES = Set.of(
        "CRYPTO_EXCHANGE",
        "GAMBLING",
        "OTHER"
    );
    private static final int MIN_HIGH_RISK_TRANSACTIONS = 2;

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

        List<Transaction> highRiskTransactions = transactions.stream()
            .filter(t -> t.getMerchantCategory() != null)
            .filter(t -> HIGH_RISK_CATEGORIES.contains(t.getMerchantCategory().toUpperCase()))
            .toList();

        if (highRiskTransactions.size() >= MIN_HIGH_RISK_TRANSACTIONS) {
            Set<String> categories = new HashSet<>();
            highRiskTransactions.forEach(t -> categories.add(t.getMerchantCategory()));

            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "HIGH",
                String.format("Account %s has %d transactions with high-risk merchant categories: %s",
                    account.getAccountId(), highRiskTransactions.size(), categories),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}

