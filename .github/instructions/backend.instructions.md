---
applyTo: "backend/**,src/main/java/**,*.java,pom.xml"
model: claude-sonnet-4-20250514
---

# Backend Instructions — Java 21 + Spring Boot 3

## Project Structure

```
backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/aml/trustwatch/
│   │   │       ├── TrustwatchApplication.java
│   │   │       ├── controller/
│   │   │       │   ├── AccountController.java
│   │   │       │   ├── TransactionController.java
│   │   │       │   ├── AlertController.java
│   │   │       │   └── HealthController.java
│   │   │       ├── service/
│   │   │       │   ├── AccountService.java
│   │   │       │   ├── TransactionService.java
│   │   │       │   ├── AlertService.java
│   │   │       │   └── RuleEngineService.java
│   │   │       ├── model/
│   │   │       │   ├── Account.java
│   │   │       │   ├── Transaction.java
│   │   │       │   ├── AmlFlag.java
│   │   │       │   ├── RuleMatch.java
│   │   │       │   └── response/
│   │   │       │       ├── ApiResponse.java
│   │   │       │       ├── ApiMeta.java
│   │   │       │       └── ApiError.java
│   │   │       ├── loader/
│   │   │       │   ├── JsonDataLoader.java
│   │   │       │   ├── AccountLoader.java
│   │   │       │   ├── TransactionLoader.java
│   │   │       │   └── AmlFlagLoader.java
│   │   │       ├── rules/
│   │   │       │   ├── Rule.java
│   │   │       │   ├── RuleEngine.java
│   │   │       │   ├── VelocityRule.java
│   │   │       │   ├── StructuringRule.java
│   │   │       │   ├── CircularFlowRule.java
│   │   │       │   └── AmountThresholdRule.java
│   │   │       ├── config/
│   │   │       │   ├── WebConfig.java
│   │   │       │   └── JacksonConfig.java
│   │   │       └── exception/
│   │   │           ├── GlobalExceptionHandler.java
│   │   │           ├── ResourceNotFoundException.java
│   │   │           └── DataLoadException.java
│   │   └── resources/
│   │       ├── application.yml
│   │       └── data/
│   │           ├── accounts.json
│   │           ├── transactions.json
│   │           ├── transaction-edges.json
│   │           └── aml-flags.json
│   └── test/
│       └── java/
│           └── com/aml/trustwatch/
│               ├── controller/
│               ├── service/
│               └── rules/
├── pom.xml
└── README.md
```

## Always

- Use **Java 21** features (records, pattern matching, virtual threads where applicable)
- Use **explicit constructors, getters, and setters** — no Lombok
- Load data from **static JSON files** using `ClassPathResource` + Jackson `ObjectMapper`
- Cache loaded JSON data at startup using `@PostConstruct`
- Return **structured API responses** with `data`, `meta`, and `errors`
- Use **constructor injection** for dependencies
- Apply **`@RestController`** for API endpoints
- Apply **`@Service`** for business logic
- Use **Java records** for immutable DTOs and response objects
- Follow **Google Java Style Guide**

## Never

- **NEVER use a database** — no PostgreSQL, MySQL, H2, MongoDB, etc.
- **NEVER use JPA, Hibernate, or Spring Data**
- **NEVER use Lombok** — write explicit code
- **NEVER expose internal exceptions** to API responses
- **NEVER use `@Autowired` on fields** — use constructor injection
- **NEVER return raw entity objects** — use response wrappers

## Model Classes

### Domain Model (No Lombok)

```java
package com.aml.trustwatch.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public class Account {
    private String accountId;
    private String accountName;
    private String accountType;
    private BigDecimal balance;
    private String currency;
    private Instant createdAt;
    private int riskScore;
    private List<String> connectedAccountIds;

    // Default constructor for Jackson
    public Account() {
    }

    // All-args constructor
    public Account(String accountId, String accountName, String accountType,
                   BigDecimal balance, String currency, Instant createdAt,
                   int riskScore, List<String> connectedAccountIds) {
        this.accountId = accountId;
        this.accountName = accountName;
        this.accountType = accountType;
        this.balance = balance;
        this.currency = currency;
        this.createdAt = createdAt;
        this.riskScore = riskScore;
        this.connectedAccountIds = connectedAccountIds;
    }

    // Getters
    public String getAccountId() { return accountId; }
    public String getAccountName() { return accountName; }
    public String getAccountType() { return accountType; }
    public BigDecimal getBalance() { return balance; }
    public String getCurrency() { return currency; }
    public Instant getCreatedAt() { return createdAt; }
    public int getRiskScore() { return riskScore; }
    public List<String> getConnectedAccountIds() { return connectedAccountIds; }

    // Setters
    public void setAccountId(String accountId) { this.accountId = accountId; }
    public void setAccountName(String accountName) { this.accountName = accountName; }
    public void setAccountType(String accountType) { this.accountType = accountType; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setRiskScore(int riskScore) { this.riskScore = riskScore; }
    public void setConnectedAccountIds(List<String> connectedAccountIds) { 
        this.connectedAccountIds = connectedAccountIds; 
    }
}
```

### Use Records for DTOs and Responses

```java
package com.aml.trustwatch.model.response;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ApiResponse<T>(
    T data,
    ApiMeta meta,
    List<ApiError> errors
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
            data,
            new ApiMeta(Instant.now().toString(), UUID.randomUUID().toString()),
            List.of()
        );
    }

    public static <T> ApiResponse<T> error(List<ApiError> errors) {
        return new ApiResponse<>(
            null,
            new ApiMeta(Instant.now().toString(), UUID.randomUUID().toString()),
            errors
        );
    }
}

public record ApiMeta(String timestamp, String requestId) {}

public record ApiError(String code, String message, String field) {
    public ApiError(String code, String message) {
        this(code, message, null);
    }
}
```

## JSON Data Loader Pattern

### Base Loader

```java
package com.aml.trustwatch.loader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public abstract class JsonDataLoader<T> {
    
    protected final ObjectMapper objectMapper;
    
    protected JsonDataLoader(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    
    protected List<T> loadFromFile(String filePath, TypeReference<List<T>> typeRef) {
        try {
            ClassPathResource resource = new ClassPathResource(filePath);
            try (InputStream inputStream = resource.getInputStream()) {
                return objectMapper.readValue(inputStream, typeRef);
            }
        } catch (IOException e) {
            throw new DataLoadException("Failed to load data from: " + filePath, e);
        }
    }
}
```

### Specific Loader Implementation

```java
package com.aml.trustwatch.loader;

import com.aml.trustwatch.model.Account;
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
public class AccountLoader extends JsonDataLoader<Account> {
    
    private static final Logger log = LoggerFactory.getLogger(AccountLoader.class);
    private static final String DATA_FILE = "data/accounts.json";
    
    private List<Account> accounts = Collections.emptyList();
    private Map<String, Account> accountsById = Collections.emptyMap();

    public AccountLoader(ObjectMapper objectMapper) {
        super(objectMapper);
    }

    @PostConstruct
    public void init() {
        log.info("Loading accounts from {}", DATA_FILE);
        this.accounts = loadFromFile(DATA_FILE, new TypeReference<>() {});
        this.accountsById = accounts.stream()
            .collect(Collectors.toMap(Account::getAccountId, Function.identity()));
        log.info("Loaded {} accounts", accounts.size());
    }

    public List<Account> getAllAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Optional<Account> getAccountById(String accountId) {
        return Optional.ofNullable(accountsById.get(accountId));
    }
}
```

## Service Layer

```java
package com.aml.trustwatch.service;

import com.aml.trustwatch.exception.ResourceNotFoundException;
import com.aml.trustwatch.loader.AccountLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.rules.RuleEngine;
import com.aml.trustwatch.model.RuleMatch;
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

    public List<Account> getHighRiskAccounts(int minRiskScore) {
        return accountLoader.getAllAccounts().stream()
            .filter(a -> a.getRiskScore() >= minRiskScore)
            .toList();
    }

    public List<RuleMatch> analyzeAccount(String accountId) {
        Account account = getAccountById(accountId);
        return ruleEngine.evaluate(account);
    }
}
```

## Controller Layer

```java
package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.model.response.ApiResponse;
import com.aml.trustwatch.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/high-risk")
    public ResponseEntity<ApiResponse<List<Account>>> getHighRiskAccounts(
            @RequestParam(defaultValue = "70") int minScore) {
        List<Account> accounts = accountService.getHighRiskAccounts(minScore);
        return ResponseEntity.ok(ApiResponse.success(accounts));
    }

    @GetMapping("/{accountId}/analyze")
    public ResponseEntity<ApiResponse<List<RuleMatch>>> analyzeAccount(@PathVariable String accountId) {
        List<RuleMatch> matches = accountService.analyzeAccount(accountId);
        return ResponseEntity.ok(ApiResponse.success(matches));
    }
}
```

## Rule Engine Implementation

### Rule Interface

```java
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
```

### Rule Engine

```java
package com.aml.trustwatch.rules;

import com.aml.trustwatch.loader.TransactionLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.RuleMatch;
import com.aml.trustwatch.model.Transaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RuleEngine {
    
    private final List<Rule> rules;
    private final TransactionLoader transactionLoader;

    public RuleEngine(List<Rule> rules, TransactionLoader transactionLoader) {
        this.rules = rules;
        this.transactionLoader = transactionLoader;
    }

    public List<RuleMatch> evaluate(Account account) {
        List<Transaction> transactions = transactionLoader.getTransactionsForAccount(account.getAccountId());
        
        return rules.stream()
            .map(rule -> rule.evaluate(account, transactions))
            .filter(Optional::isPresent)
            .map(Optional::get)
            .toList();
    }
}
```

### Example Rule Implementation

```java
package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class VelocityRule implements Rule {
    
    private static final String RULE_ID = "VELOCITY_001";
    private static final String RULE_NAME = "High Velocity Transactions";
    private static final int MAX_TRANSACTIONS_PER_HOUR = 10;

    @Override
    public String getRuleId() { return RULE_ID; }

    @Override
    public String getRuleName() { return RULE_NAME; }

    @Override
    public Optional<RuleMatch> evaluate(Account account, List<Transaction> transactions) {
        Instant oneHourAgo = Instant.now().minus(Duration.ofHours(1));
        
        long recentTransactionCount = transactions.stream()
            .filter(t -> t.getTimestamp().isAfter(oneHourAgo))
            .count();

        if (recentTransactionCount > MAX_TRANSACTIONS_PER_HOUR) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "HIGH",
                String.format("Account has %d transactions in the last hour (threshold: %d)",
                    recentTransactionCount, MAX_TRANSACTIONS_PER_HOUR),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}
```

### Structuring Rule

```java
package com.aml.trustwatch.rules;

import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.model.Transaction;
import com.aml.trustwatch.model.RuleMatch;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Component
public class StructuringRule implements Rule {
    
    private static final String RULE_ID = "STRUCT_001";
    private static final String RULE_NAME = "Potential Structuring";
    private static final BigDecimal REPORTING_THRESHOLD = new BigDecimal("10000");
    private static final BigDecimal STRUCTURING_LOWER_BOUND = new BigDecimal("9000");
    private static final int MIN_SUSPICIOUS_TRANSACTIONS = 3;

    @Override
    public String getRuleId() { return RULE_ID; }

    @Override
    public String getRuleName() { return RULE_NAME; }

    @Override
    public Optional<RuleMatch> evaluate(Account account, List<Transaction> transactions) {
        Instant twentyFourHoursAgo = Instant.now().minus(Duration.ofHours(24));
        
        long suspiciousCount = transactions.stream()
            .filter(t -> t.getTimestamp().isAfter(twentyFourHoursAgo))
            .filter(t -> t.getAmount().compareTo(STRUCTURING_LOWER_BOUND) >= 0)
            .filter(t -> t.getAmount().compareTo(REPORTING_THRESHOLD) < 0)
            .count();

        if (suspiciousCount >= MIN_SUSPICIOUS_TRANSACTIONS) {
            return Optional.of(new RuleMatch(
                RULE_ID,
                RULE_NAME,
                "CRITICAL",
                String.format("Account has %d transactions just below reporting threshold in 24h",
                    suspiciousCount),
                Instant.now()
            ));
        }

        return Optional.empty();
    }
}
```

## Exception Handling

```java
package com.aml.trustwatch.exception;

import com.aml.trustwatch.model.response.ApiError;
import com.aml.trustwatch.model.response.ApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceNotFound(ResourceNotFoundException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(ApiResponse.error(List.of(
                new ApiError("NOT_FOUND", ex.getMessage())
            )));
    }

    @ExceptionHandler(DataLoadException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataLoadException(DataLoadException ex) {
        log.error("Data load error", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(List.of(
                new ApiError("DATA_LOAD_ERROR", "Failed to load data")
            )));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGenericException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(ApiResponse.error(List.of(
                new ApiError("INTERNAL_ERROR", "An unexpected error occurred")
            )));
    }
}
```

```java
package com.aml.trustwatch.exception;

public class ResourceNotFoundException extends RuntimeException {
    
    public ResourceNotFoundException(String resourceType, String id) {
        super(String.format("%s not found with id: %s", resourceType, id));
    }
}
```

```java
package com.aml.trustwatch.exception;

public class DataLoadException extends RuntimeException {
    
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
```

## Configuration

### application.yml

```yaml
server:
  port: 8080

spring:
  application:
    name: trustwatch-aml

logging:
  level:
    com.aml.trustwatch: INFO
    
# CORS configuration for Angular frontend
cors:
  allowed-origins: http://localhost:4200
```

### CORS Configuration

```java
package com.aml.trustwatch.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*");
            }
        };
    }
}
```

### Jackson Configuration

```java
package com.aml.trustwatch.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }
}
```

## Health Check Endpoint

```java
package com.aml.trustwatch.controller;

import com.aml.trustwatch.model.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    @GetMapping("/health")
    public ResponseEntity<ApiResponse<Map<String, String>>> health() {
        return ResponseEntity.ok(ApiResponse.success(Map.of(
            "status", "UP",
            "service", "trustwatch-aml"
        )));
    }
}
```

## Security & Performance

- Validate all path variables and request parameters
- Use pagination for list endpoints when data grows
- Cache loaded JSON data at application startup
- Use `@Cacheable` for computed results if needed
- Never log sensitive account information
- Sanitize log messages to prevent log injection

## Testing Conventions

```java
package com.aml.trustwatch.service;

import com.aml.trustwatch.loader.AccountLoader;
import com.aml.trustwatch.model.Account;
import com.aml.trustwatch.rules.RuleEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountLoader accountLoader;
    
    @Mock
    private RuleEngine ruleEngine;

    private AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = new AccountService(accountLoader, ruleEngine);
    }

    @Test
    void shouldReturnAllAccounts() {
        // Given
        List<Account> expectedAccounts = List.of(createTestAccount("ACC-001"));
        when(accountLoader.getAllAccounts()).thenReturn(expectedAccounts);

        // When
        List<Account> result = accountService.getAllAccounts();

        // Then
        assertThat(result).isEqualTo(expectedAccounts);
    }

    private Account createTestAccount(String id) {
        Account account = new Account();
        account.setAccountId(id);
        account.setAccountName("Test Account");
        return account;
    }
}
```

## Maven Dependencies (pom.xml essentials)

```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>com.fasterxml.jackson.datatype</groupId>
        <artifactId>jackson-datatype-jsr310</artifactId>
    </dependency>
    
    <!-- Test dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

**Note:** Do NOT add spring-boot-starter-data-jpa or any database driver.

