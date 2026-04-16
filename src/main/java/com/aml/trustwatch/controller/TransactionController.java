package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.response.ApiResponse;
import com.aml.trustwatch.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Transaction>>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<ApiResponse<Transaction>> getTransactionById(@PathVariable String transactionId) {
        Transaction transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(ApiResponse.success(transaction));
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsForAccount(@PathVariable String accountId) {
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<Transaction>>> getTransactionsByStatus(@PathVariable String status) {
        List<Transaction> transactions = transactionService.getTransactionsByStatus(status);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/high-risk")
    public ResponseEntity<ApiResponse<List<Transaction>>> getHighRiskTransactions(
            @RequestParam(defaultValue = "70") int minScore) {
        List<Transaction> transactions = transactionService.getHighRiskTransactions(minScore);
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/pending")
    public ResponseEntity<ApiResponse<List<Transaction>>> getPendingReviewTransactions() {
        List<Transaction> transactions = transactionService.getPendingReviewTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }

    @GetMapping("/blocked")
    public ResponseEntity<ApiResponse<List<Transaction>>> getBlockedTransactions() {
        List<Transaction> transactions = transactionService.getBlockedTransactions();
        return ResponseEntity.ok(ApiResponse.success(transactions));
    }
}

