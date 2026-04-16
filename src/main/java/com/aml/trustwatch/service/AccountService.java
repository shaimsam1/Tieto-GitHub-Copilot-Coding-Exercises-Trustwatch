package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.AccountLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.rules.RuleEngine;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    
    private final AccountLoader accountLoader;
    private final RuleEngine ruleEngine;

    public AccountService(AccountLoader accountLoader, RuleEngine ruleEngine) {
        this.accountLoader = accountLoader;
        this.ruleEngine = ruleEngine;
    }

    public List<Account> getAllAccounts() {
        return accountLoader.getAllAccounts();
    }

    public Account getAccountById(String accountId) {
        return accountLoader.getAccountById(accountId)
            .orElseThrow(() -> new ResourceNotFoundException("Account", accountId));
    }

    public List<Account> getAccountsByRiskRating(String riskRating) {
        return accountLoader.getAccountsByRiskRating(riskRating);
    }
    
    public List<Account> getHighRiskAccounts() {
        return accountLoader.getAccountsByRiskRating("HIGH");
    }
    
    public List<Account> getAccountsWithFlags() {
        return accountLoader.getAccountsWithFlags();
    }

    public List<RuleMatch> analyzeAccount(String accountId) {
        Account account = getAccountById(accountId);
        return ruleEngine.evaluate(account);
    }
}

