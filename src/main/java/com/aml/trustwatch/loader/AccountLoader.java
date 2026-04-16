package com.aml.trustwatch.loader;

import com.aml.trustwatch.model.Account;
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
public class AccountLoader extends JsonDataLoader<Account> {

    private static final Logger log = LoggerFactory.getLogger(AccountLoader.class);
    private static final String DATA_FILE = "data/accounts.json";

    private List<Account> accounts = Collections.emptyList();
    private Map<String, Account> accountsById = Collections.emptyMap();

    public AccountLoader(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() {
        log.info("Loading accounts from {}", DATA_FILE);
        this.accounts = loadFromFile(DATA_FILE, new TypeReference<>() {});
        this.accountsById = accounts.stream()
            .collect(Collectors.toMap(Account::getAccountId, Function.identity()));
        log.info("Loaded {} accounts", accounts.size());
    }

    public List<Account> getAllAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Optional<Account> getAccountById(String accountId) {
        return Optional.ofNullable(accountsById.get(accountId));
    }

    public List<Account> getAccountsByRiskRating(String riskRating) {
        return accounts.stream()
            .filter(a -> a.getRiskRating().equalsIgnoreCase(riskRating))
            .toList();
    }

    public List<Account> getAccountsWithFlags() {
        return accounts.stream()
            .filter(a -> a.getAmlFlags() != null && !a.getAmlFlags().isEmpty())
            .toList();
    }
}

