package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.model.response.ApiResponse;
import com.aml.trustwatch.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Account>>> getAllAccounts() {
        List<Account> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/{accountId}")
    public ResponseEntity<ApiResponse<Account>> getAccountById(@PathVariable String accountId) {
        Account account = accountService.getAccountById(accountId);
        return ResponseEntity.ok(ApiResponse.success(account));
    }

    @GetMapping("/risk/{riskRating}")
    public ResponseEntity<ApiResponse<List<Account>>> getAccountsByRiskRating(@PathVariable String riskRating) {
        List<Account> accounts = accountService.getAccountsByRiskRating(riskRating);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/high-risk")
    public ResponseEntity<ApiResponse<List<Account>>> getHighRiskAccounts() {
        List<Account> accounts = accountService.getHighRiskAccounts();
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/flagged")
    public ResponseEntity<ApiResponse<List<Account>>> getAccountsWithFlags() {
        List<Account> accounts = accountService.getAccountsWithFlags();
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/{accountId}/analyze")
    public ResponseEntity<ApiResponse<List<RuleMatch>>> analyzeAccount(@PathVariable String accountId) {
        List<RuleMatch> matches = accountService.analyzeAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(matches));
    }
}

