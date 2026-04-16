package com.aml.trustwatch.loader;

import com.aml.trustwatch.model.Transaction;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class TransactionLoader extends JsonDataLoader<Transaction> {

    private static final Logger log = LoggerFactory.getLogger(TransactionLoader.class);
    private static final String DATA_FILE = "data/transactions.json";

    private List<Transaction> transactions = Collections.emptyList();
    private Map<String, Transaction> transactionsById = Collections.emptyMap();
    private Map<String, List<Transaction>> transactionsByAccountId = Collections.emptyMap();

    public TransactionLoader(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() {
        log.info("Loading transactions from {}", DATA_FILE);
        this.transactions = loadFromFile(DATA_FILE, new TypeReference<>() {});
        this.transactionsById = transactions.stream()
            .collect(Collectors.toMap(Transaction::getTransactionId, Function.identity()));
        this.transactionsByAccountId = transactions.stream()
            .collect(Collectors.groupingBy(Transaction::getAccountId));
        log.info("Loaded {} transactions", transactions.size());
    }

    public List<Transaction> getAllTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public Optional<Transaction> getTransactionById(String transactionId) {
        return Optional.ofNullable(transactionsById.get(transactionId));
    }

    public List<Transaction> getTransactionsForAccount(String accountId) {
        return transactionsByAccountId.getOrDefault(accountId, Collections.emptyList());
    }

    public List<Transaction> getTransactionsByStatus(String status) {
        return transactions.stream()
            .filter(t -> t.getStatus().equalsIgnoreCase(status))
            .toList();
    }

    public List<Transaction> getHighRiskTransactions(int minRiskScore) {
        return transactions.stream()
            .filter(t -> t.getRiskScore() >= minRiskScore)
            .toList();
    }
}

