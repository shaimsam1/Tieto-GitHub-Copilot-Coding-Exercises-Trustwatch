package com.aml.trustwatch.rules;

import com.aml.trustwatch.loader.TransactionLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RuleEngine {

    private final List<Rule> rules;
    private final TransactionLoader transactionLoader;

    public RuleEngine(List<Rule> rules, TransactionLoader transactionLoader) {
        this.rules = rules;
        this.transactionLoader = transactionLoader;
    }

    public List<RuleMatch> evaluate(Account account) {
        List<Transaction> transactions = transactionLoader.getTransactionsForAccount(account.getAccountId());

        return rules.stream()
            .map(rule -> rule.evaluate(account, transactions))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }

    public List<RuleMatch> evaluateWithTransactions(Account account, List<Transaction> transactions) {
        return rules.stream()
            .map(rule -> rule.evaluate(account, transactions))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }
}

