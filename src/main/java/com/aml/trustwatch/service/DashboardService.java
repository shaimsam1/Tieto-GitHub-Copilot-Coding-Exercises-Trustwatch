package com.aml.trustwatch.service;

import com.aml.trustwatch.loader.AccountLoader;
import com.aml.trustwatch.loader.AmlFlagLoader;
import com.aml.trustwatch.loader.TransactionLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.AmlFlagRecord;
import com.aml.trustwatch.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final AccountLoader accountLoader;
    private final TransactionLoader transactionLoader;
    private final AmlFlagLoader amlFlagLoader;

    public DashboardService(AccountLoader accountLoader, TransactionLoader transactionLoader,
                           AmlFlagLoader amlFlagLoader) {
        this.accountLoader = accountLoader;
        this.transactionLoader = transactionLoader;
        this.amlFlagLoader = amlFlagLoader;
    }

    public Map<String, Object> getDashboardSummary() {
        Map<String, Object> summary = new HashMap<>();

        List<Account> accounts = accountLoader.getAllAccounts();
        List<Transaction> transactions = transactionLoader.getAllTransactions();
        List<AmlFlagRecord> flags = amlFlagLoader.getAllFlags();

        // Account statistics
        Map<String, Object> accountStats = new HashMap<>();
        accountStats.put("total", accounts.size());
        accountStats.put("highRisk", accounts.stream().filter(a -> "HIGH".equals(a.getRiskRating())).count());
        accountStats.put("mediumRisk", accounts.stream().filter(a -> "MEDIUM".equals(a.getRiskRating())).count());
        accountStats.put("lowRisk", accounts.stream().filter(a -> "LOW".equals(a.getRiskRating())).count());
        accountStats.put("withFlags", accountLoader.getAccountsWithFlags().size());
        summary.put("accounts", accountStats);

        // Transaction statistics
        Map<String, Object> transactionStats = new HashMap<>();
        transactionStats.put("total", transactions.size());
        transactionStats.put("pending", transactions.stream().filter(t -> "PENDING_REVIEW".equals(t.getStatus())).count());
        transactionStats.put("blocked", transactions.stream().filter(t -> "BLOCKED".equals(t.getStatus())).count());
        transactionStats.put("approved", transactions.stream().filter(t -> "APPROVED".equals(t.getStatus())).count());
        transactionStats.put("highRisk", transactions.stream().filter(t -> t.getRiskScore() >= 70).count());
        summary.put("transactions", transactionStats);

        // Alert statistics
        Map<String, Object> alertStats = new HashMap<>();
        alertStats.put("total", flags.size());
        alertStats.put("critical", flags.stream().filter(f -> "CRITICAL".equals(f.getSeverity())).count());
        alertStats.put("high", flags.stream().filter(f -> "HIGH".equals(f.getSeverity())).count());
        alertStats.put("medium", flags.stream().filter(f -> "MEDIUM".equals(f.getSeverity())).count());
        alertStats.put("low", flags.stream().filter(f -> "LOW".equals(f.getSeverity())).count());
        summary.put("alerts", alertStats);

        // Typology breakdown
        Map<String, Long> typologyBreakdown = new HashMap<>();
        typologyBreakdown.put("STRUCTURING", flags.stream().filter(f -> "STRUCTURING".equals(f.getTypology())).count());
        typologyBreakdown.put("LAYERING", flags.stream().filter(f -> "LAYERING".equals(f.getTypology())).count());
        typologyBreakdown.put("ROUND_TRIPPING", flags.stream().filter(f -> "ROUND_TRIPPING".equals(f.getTypology())).count());
        typologyBreakdown.put("SHELL_NETWORK", flags.stream().filter(f -> "SHELL_NETWORK".equals(f.getTypology())).count());
        summary.put("typologies", typologyBreakdown);

        return summary;
    }
}

