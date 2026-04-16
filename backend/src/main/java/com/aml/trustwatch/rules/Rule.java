package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;

import java.util.List;
import java.util.Optional;

public interface Rule {
    
    String getRuleId();
    
    String getRuleName();
    
    Optional<RuleMatch> evaluate(Account account, List<Transaction> transactions);
}

