package com.aml.trustwatch.loader;

import com.aml.trustwatch.model.TransactionEdge;
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
public class TransactionEdgeLoader extends JsonDataLoader<TransactionEdge> {

    private static final Logger log = LoggerFactory.getLogger(TransactionEdgeLoader.class);
    private static final String DATA_FILE = "data/transaction-edges.json";

    private List<TransactionEdge> edges = Collections.emptyList();
    private Map<String, TransactionEdge> edgesById = Collections.emptyMap();
    private Map<String, List<TransactionEdge>> edgesByFromAccount = Collections.emptyMap();
    private Map<String, List<TransactionEdge>> edgesByToAccount = Collections.emptyMap();

    public TransactionEdgeLoader(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() {
        log.info("Loading transaction edges from {}", DATA_FILE);
        this.edges = loadFromFile(DATA_FILE, new TypeReference<>() {});
        this.edgesById = edges.stream()
            .collect(Collectors.toMap(TransactionEdge::getEdgeId, Function.identity()));
        this.edgesByFromAccount = edges.stream()
            .collect(Collectors.groupingBy(TransactionEdge::getFromAccountId));
        this.edgesByToAccount = edges.stream()
            .collect(Collectors.groupingBy(TransactionEdge::getToAccountId));
        log.info("Loaded {} transaction edges", edges.size());
    }

    public List<TransactionEdge> getAllEdges() {
        return Collections.unmodifiableList(edges);
    }

    public Optional<TransactionEdge> getEdgeById(String edgeId) {
        return Optional.ofNullable(edgesById.get(edgeId));
    }

    public List<TransactionEdge> getEdgesFromAccount(String accountId) {
        return edgesByFromAccount.getOrDefault(accountId, Collections.emptyList());
    }

    public List<TransactionEdge> getEdgesToAccount(String accountId) {
        return edgesByToAccount.getOrDefault(accountId, Collections.emptyList());
    }

    public List<TransactionEdge> getFlaggedEdges() {
        return edges.stream()
            .filter(TransactionEdge::isFlagged)
            .toList();
    }
}

