package com.aml.trustwatch.service;

import com.aml.trustwatch.loader.TransactionEdgeLoader;
import com.aml.trustwatch.model.TransactionEdge;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class NetworkService {

    private final TransactionEdgeLoader edgeLoader;

    public NetworkService(TransactionEdgeLoader edgeLoader) {
        this.edgeLoader = edgeLoader;
    }

    public List<TransactionEdge> getAllEdges() {
        return edgeLoader.getAllEdges();
    }

    public List<TransactionEdge> getFlaggedEdges() {
        return edgeLoader.getFlaggedEdges();
    }

    public List<TransactionEdge> getEdgesForAccount(String accountId) {
        List<TransactionEdge> result = new ArrayList<>();
        result.addAll(edgeLoader.getEdgesFromAccount(accountId));
        result.addAll(edgeLoader.getEdgesToAccount(accountId));
        return result;
    }

    public Set<String> getConnectedAccounts(String accountId) {
        Set<String> connected = new HashSet<>();

        for (TransactionEdge edge : edgeLoader.getEdgesFromAccount(accountId)) {
            connected.add(edge.getToAccountId());
        }

        for (TransactionEdge edge : edgeLoader.getEdgesToAccount(accountId)) {
            connected.add(edge.getFromAccountId());
        }

        return connected;
    }

    public List<List<TransactionEdge>> detectCircularFlows() {
        List<List<TransactionEdge>> circularFlows = new ArrayList<>();
        List<TransactionEdge> flaggedEdges = edgeLoader.getFlaggedEdges();

        // Group edges by chains based on hops
        // This is a simplified detection - just finding sequences where money returns
        Set<String> processedAccounts = new HashSet<>();

        for (TransactionEdge edge : flaggedEdges) {
            if (edge.getHops() == 1 && !processedAccounts.contains(edge.getFromAccountId())) {
                List<TransactionEdge> chain = traceChain(edge.getFromAccountId(), flaggedEdges);
                if (isCircular(chain)) {
                    circularFlows.add(chain);
                    for (TransactionEdge e : chain) {
                        processedAccounts.add(e.getFromAccountId());
                    }
                }
            }
        }

        return circularFlows;
    }

    private List<TransactionEdge> traceChain(String startAccountId, List<TransactionEdge> edges) {
        List<TransactionEdge> chain = new ArrayList<>();
        String currentAccount = startAccountId;
        Set<String> visited = new HashSet<>();

        while (!visited.contains(currentAccount)) {
            visited.add(currentAccount);
            String finalCurrentAccount = currentAccount;
            TransactionEdge nextEdge = edges.stream()
                .filter(e -> e.getFromAccountId().equals(finalCurrentAccount))
                .findFirst()
                .orElse(null);

            if (nextEdge == null) {
                break;
            }

            chain.add(nextEdge);
            currentAccount = nextEdge.getToAccountId();

            if (currentAccount.equals(startAccountId)) {
                break; // Circular detected
            }
        }

        return chain;
    }

    private boolean isCircular(List<TransactionEdge> chain) {
        if (chain.isEmpty()) {
            return false;
        }
        String start = chain.get(0).getFromAccountId();
        String end = chain.get(chain.size() - 1).getToAccountId();
        return start.equals(end);
    }
}

