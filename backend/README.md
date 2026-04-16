# Trustwatch AML Backend

Anti-Money Laundering (AML) Pattern Detection Dashboard Backend - Java 21 + Spring Boot 3

## Quick Start

```bash
# Build
mvn clean compile

# Run tests
mvn test

# Run application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## API Endpoints

### Health Check
- `GET /api/health` - Service health status

### Dashboard
- `GET /api/dashboard/summary` - Dashboard statistics summary

### Accounts
- `GET /api/accounts` - Get all accounts
- `GET /api/accounts/{accountId}` - Get account by ID
- `GET /api/accounts/risk/{riskRating}` - Get accounts by risk rating
- `GET /api/accounts/high-risk` - Get high-risk accounts
- `GET /api/accounts/flagged` - Get accounts with AML flags
- `GET /api/accounts/{accountId}/analyze` - Run AML rule analysis on account

### Transactions
- `GET /api/transactions` - Get all transactions
- `GET /api/transactions/{transactionId}` - Get transaction by ID
- `GET /api/transactions/account/{accountId}` - Get transactions for account
- `GET /api/transactions/status/{status}` - Get transactions by status
- `GET /api/transactions/high-risk?minScore=70` - Get high-risk transactions
- `GET /api/transactions/pending` - Get pending review transactions
- `GET /api/transactions/blocked` - Get blocked transactions

### Alerts (AML Flags)
- `GET /api/alerts` - Get all alerts
- `GET /api/alerts/{flagId}` - Get alert by ID
- `GET /api/alerts/account/{accountId}` - Get alerts for account
- `GET /api/alerts/severity/{severity}` - Get alerts by severity
- `GET /api/alerts/typology/{typology}` - Get alerts by typology
- `GET /api/alerts/critical` - Get critical alerts
- `GET /api/alerts/high` - Get high severity alerts

### Network Analysis
- `GET /api/network/edges` - Get all transaction edges
- `GET /api/network/edges/flagged` - Get flagged edges
- `GET /api/network/account/{accountId}/edges` - Get edges for account
- `GET /api/network/account/{accountId}/connected` - Get connected accounts
- `GET /api/network/circular-flows` - Detect circular fund flows

## API Response Format

All responses follow this envelope:

```json
{
  "data": { },
  "meta": {
    "timestamp": "2026-04-16T10:30:00Z",
    "requestId": "uuid"
  },
  "errors": []
}
```

## AML Rules Implemented

| Rule ID | Rule Name | Description |
|---------|-----------|-------------|
| VELOCITY_001 | High Velocity Transactions | Detects unusual transaction frequency |
| STRUCT_001 | Potential Structuring | Identifies split transactions avoiding thresholds |
| AMOUNT_001 | Large Transaction Amount | Flags transactions exceeding defined limits |
| MERCHANT_001 | High Risk Merchant Activity | Detects transactions with high-risk merchants |
| GEO_001 | Geographic Anomaly Detection | Finds transactions to high-risk jurisdictions |

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
│   │   │       │   ├── NetworkController.java
│   │   │       │   ├── DashboardController.java
│   │   │       │   └── HealthController.java
│   │   │       ├── service/
│   │   │       │   ├── AccountService.java
│   │   │       │   ├── TransactionService.java
│   │   │       │   ├── AlertService.java
│   │   │       │   ├── NetworkService.java
│   │   │       │   └── DashboardService.java
│   │   │       ├── model/
│   │   │       │   ├── Account.java
│   │   │       │   ├── Transaction.java
│   │   │       │   ├── AmlFlag.java
│   │   │       │   ├── AmlFlagRecord.java
│   │   │       │   ├── TransactionEdge.java
│   │   │       │   ├── RuleMatch.java
│   │   │       │   └── response/
│   │   │       │       ├── ApiResponse.java
│   │   │       │       ├── ApiMeta.java
│   │   │       │       └── ApiError.java
│   │   │       ├── loader/
│   │   │       │   ├── JsonDataLoader.java
│   │   │       │   ├── AccountLoader.java
│   │   │       │   ├── TransactionLoader.java
│   │   │       │   ├── AmlFlagLoader.java
│   │   │       │   └── TransactionEdgeLoader.java
│   │   │       ├── rules/
│   │   │       │   ├── Rule.java
│   │   │       │   ├── RuleEngine.java
│   │   │       │   ├── VelocityRule.java
│   │   │       │   ├── StructuringRule.java
│   │   │       │   ├── AmountThresholdRule.java
│   │   │       │   ├── HighRiskMerchantRule.java
│   │   │       │   └── GeographicAnomalyRule.java
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
│               │   └── AccountControllerTest.java
│               └── service/
│                   ├── AccountServiceTest.java
│                   ├── TransactionServiceTest.java
│                   └── AlertServiceTest.java
└── pom.xml
```

## Technology Stack

- Java 21
- Spring Boot 3.2.4
- Jackson for JSON processing
- JUnit 5 + Mockito for testing
- AssertJ for assertions

## Constraints

- NO database - all data loaded from static JSON files
- NO JPA/Hibernate/Spring Data - plain POJOs
- NO Lombok - explicit constructors, getters, setters
- Constructor injection only - no @Autowired on fields

