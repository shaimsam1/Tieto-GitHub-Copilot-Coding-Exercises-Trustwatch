package com.aml.trustwatch.loader;

import com.aml.trustwatch.model.AmlFlagRecord;
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
public class AmlFlagLoader extends JsonDataLoader<AmlFlagRecord> {

    private static final Logger log = LoggerFactory.getLogger(AmlFlagLoader.class);
    private static final String DATA_FILE = "data/aml-flags.json";

    private List<AmlFlagRecord> flags = Collections.emptyList();
    private Map<String, AmlFlagRecord> flagsById = Collections.emptyMap();
    private Map<String, List<AmlFlagRecord>> flagsByAccountId = Collections.emptyMap();

    public AmlFlagLoader(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() {
        log.info("Loading AML flags from {}", DATA_FILE);
        this.flags = loadFromFile(DATA_FILE, new TypeReference<>() {});
        this.flagsById = flags.stream()
            .collect(Collectors.toMap(AmlFlagRecord::getFlagId, Function.identity()));
        this.flagsByAccountId = flags.stream()
            .collect(Collectors.groupingBy(AmlFlagRecord::getAccountId));
        log.info("Loaded {} AML flags", flags.size());
    }

    public List<AmlFlagRecord> getAllFlags() {
        return Collections.unmodifiableList(flags);
    }

    public Optional<AmlFlagRecord> getFlagById(String flagId) {
        return Optional.ofNullable(flagsById.get(flagId));
    }

    public List<AmlFlagRecord> getFlagsForAccount(String accountId) {
        return flagsByAccountId.getOrDefault(accountId, Collections.emptyList());
    }

    public List<AmlFlagRecord> getFlagsBySeverity(String severity) {
        return flags.stream()
            .filter(f -> f.getSeverity().equalsIgnoreCase(severity))
            .toList();
    }

    public List<AmlFlagRecord> getFlagsByTypology(String typology) {
        return flags.stream()
            .filter(f -> f.getTypology().equalsIgnoreCase(typology))
            .toList();
    }
}

