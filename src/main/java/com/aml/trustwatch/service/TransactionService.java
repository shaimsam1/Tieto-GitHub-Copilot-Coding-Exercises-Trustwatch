package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.TransactionLoader;
import com.aml.trustwatch.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionLoader transactionLoader;

    public TransactionService(TransactionLoader transactionLoader) {
        this.transactionLoader = transactionLoader;
    }

    public List<Transaction> getAllTransactions() {
        return transactionLoader.getAllTransactions();
    }

    public Transaction getTransactionById(String transactionId) {
        return transactionLoader.getTransactionById(transactionId)
            .orElseThrow(() -> new ResourceNotFoundException("Transaction", transactionId));
    }

    public List<Transaction> getTransactionsForAccount(String accountId) {
        return transactionLoader.getTransactionsForAccount(accountId);
    }

    public List<Transaction> getTransactionsByStatus(String status) {
        return transactionLoader.getTransactionsByStatus(status);
    }

    public List<Transaction> getHighRiskTransactions(int minRiskScore) {
        return transactionLoader.getHighRiskTransactions(minRiskScore);
    }

    public List<Transaction> getPendingReviewTransactions() {
        return transactionLoader.getTransactionsByStatus("PENDING_REVIEW");
    }

    public List<Transaction> getBlockedTransactions() {
        return transactionLoader.getTransactionsByStatus("BLOCKED");
    }
}

