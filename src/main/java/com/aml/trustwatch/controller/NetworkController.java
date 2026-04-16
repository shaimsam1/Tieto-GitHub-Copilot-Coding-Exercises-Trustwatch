package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.TransactionEdge;
import com.aml.trustwatch.model.response.ApiResponse;
import com.aml.trustwatch.service.NetworkService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/network")
public class NetworkController {

    private final NetworkService networkService;

    public NetworkController(NetworkService networkService) {
        this.networkService = networkService;
    }

    @GetMapping("/edges")
    public ResponseEntity<ApiResponse<List<TransactionEdge>>> getAllEdges() {
        List<TransactionEdge> edges = networkService.getAllEdges();
        return ResponseEntity.ok(ApiResponse.success(edges));
    }

    @GetMapping("/edges/flagged")
    public ResponseEntity<ApiResponse<List<TransactionEdge>>> getFlaggedEdges() {
        List<TransactionEdge> edges = networkService.getFlaggedEdges();
        return ResponseEntity.ok(ApiResponse.success(edges));
    }

    @GetMapping("/account/{accountId}/edges")
    public ResponseEntity<ApiResponse<List<TransactionEdge>>> getEdgesForAccount(@PathVariable String accountId) {
        List<TransactionEdge> edges = networkService.getEdgesForAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(edges));
    }

    @GetMapping("/account/{accountId}/connected")
    public ResponseEntity<ApiResponse<Set<String>>> getConnectedAccounts(@PathVariable String accountId) {
        Set<String> connected = networkService.getConnectedAccounts(accountId);
        return ResponseEntity.ok(ApiResponse.success(connected));
    }

    @GetMapping("/circular-flows")
    public ResponseEntity<ApiResponse<List<List<TransactionEdge>>>> detectCircularFlows() {
        List<List<TransactionEdge>> flows = networkService.detectCircularFlows();
        return ResponseEntity.ok(ApiResponse.success(flows));
    }
}

