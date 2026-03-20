# Digicore Wallet System API

**Author:** Hilary Emujede  
**Email:** hilaryemujede48@gmail.com

A RESTful Wallet API built with Spring Boot 3+ that allows users to create wallets, fund them, and perform debit transactions with full transaction history tracking.

---

## Technology Stack

| Layer         | Technology                          |
|---------------|-------------------------------------|
| Language      | Java 17+                            |
| Framework     | Spring Boot 4.x.x                   |
| Database      | H2 (in-memory, auto-configured)     |
| ORM           | Spring Data JPA / Hibernate         |
| Validation    | Jakarta Bean Validation (`@Valid`)  |
| API Docs      | SpringDoc OpenAPI 2 (Swagger UI)    |
| Build Tool    | Maven                               |
| Utilities     | Lombok                              |
| Testing       | JUnit 5 + Mockito + AssertJ         |

---

## Project Structure

```
Digicore-Wallet-System-API/
├── .mvn/wrapper/
│   └── maven-wrapper.properties
├── src/
│   ├── main/
│   │   ├── java/com/wallet/system/api/
│   │   │   ├── WalletSystemApiApplication.java         # Application entry point
│   │   │   ├── config/
│   │   │   │   └── OpenApiConfig.java                 # Swagger / OpenAPI bean
│   │   │   ├── shared/
│   │   │   │   ├── error/
│   │   │   │   │   └── ErrorResponse.java
│   │   │   │   ├── exception/
│   │   │   │   │   ├── GlobalExceptionHandler.java
│   │   │   │   │   ├── InsufficientFundsException.java
│   │   │   │   │   └── WalletNotFoundException.java
│   │   │   │   └── ApiResponse.java
│   │   │   ├── transaction/
│   │   │   │   ├── controller/
│   │   │   │   │   └── TransactionController.java
│   │   │   │   ├── dto/
│   │   │   │   │   └── TransactionResponse.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── Transaction.java
│   │   │   │   ├── enums/
│   │   │   │   │   └── TransactionType.java
│   │   │   │   ├── mapper/
│   │   │   │   │   └── TransactionMapper.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── TransactionRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── TransactionService.java
│   │   │   │       └── TransactionServiceImpl.java
│   │   │   └── wallet/
│   │   │       ├── controller/
│   │   │       │   └── WalletController.java
│   │   │       ├── dto/
│   │   │       │   ├── AmountRequest.java
│   │   │       │   ├── CreateWalletRequest.java
│   │   │       │   ├── WalletResponse.java
│   │   │       │   └── WalletWithHistoryResponse.java
│   │   │       ├── entity/
│   │   │       │   └── Wallet.java
│   │   │       ├── mapper/
│   │   │       │   └── WalletMapper.java
│   │   │       ├── repository/
│   │   │       │   └── WalletRepository.java
│   │   │       └── service/
│   │   │           ├── WalletService.java
│   │   │           └── WalletServiceImpl.java
│   │   └── resources/
│   │       └── application.properties                # App & DB configuration
│   └── test/
│       └── java/com/wallet/system/api/
│           ├── WalletServiceTest.java               # Unit tests for WalletService
│           └── WalletSystemApiApplicationTests.java # Spring Boot app context tests
├── .gitattributes
├── .gitignore
├── mvnw                                           # Maven wrapper (Unix)
├── mvnw.cmd                                       # Maven wrapper (Windows)
├── pom.xml                                        # Dependencies & build config
└── README.md
```

---

## Build & Run

### Prerequisites
- Java 17 or higher
- Maven 3.8+ (or use the included `./mvnw` wrapper)

### Steps

```bash
# 1. Clone the repository
git clone <repo-url>
cd Digicore-Wallet-System-API

# 2. Build the project
mvn clean package

# 3. Run the application
mvn spring-boot:run
# OR
java -jar target/wallet-api-1.0.0.jar
```

The server starts on **http://localhost:8080**

---

## API Endpoints

| Method | Endpoint              | Description                    |
|--------|-----------------------|--------------------------------|
| POST   | `/wallets`            | Create a new wallet            |
| POST   | `/wallets/{id}/fund`  | Fund (credit) a wallet         |
| POST   | `/wallets/{id}/debit` | Debit (spend from) a wallet    |
| GET    | `/wallets/{id}`       | Get wallet + transaction history |
| GET    | `/transactions/{id}`  | Get transaction history |

### Example Requests

**Create wallet**
```http
POST /wallets
Content-Type: application/json

{ "userId": "user-123" }
```

**Fund wallet**
```http
POST /wallets/1/fund
Content-Type: application/json

{ "amount": 1000.00 }
```

**Debit wallet**
```http
POST /wallets/1/debit
Content-Type: application/json

{ "amount": 250.00 }
```

**Get wallet**
```http
GET /wallets/1

```

**Get transaction history for a wallet**
```http
GET /transactions/1

```

---

## Swagger UI

Once the app is running, navigate to:

```
http://localhost:8080/swagger-ui.html
```

All endpoints are fully documented with request/response schemas and are directly testable from the browser.

Raw OpenAPI spec is available at:
```
http://localhost:8080/api-docs
```

---

## H2 Console

The H2 in-memory database console is accessible at:

```
http://localhost:8080/h2-console
```

| Field    | Value                                        |
|----------|----------------------------------------------|
| JDBC URL | `jdbc:h2:mem:walletdb`                       |
| Username | `sa`                                         |
| Password | *(leave blank)*                              |

---

## Running Tests

```bash
mvn test
```

---

## Business Rules

- A wallet is created with a **zero balance**
- Funding and debit **amounts must be greater than 0** (enforced by `@Positive`)
- Debiting **beyond available balance** returns `400 INSUFFICIENT_FUNDS`
- All monetary values use **`BigDecimal`** to avoid floating-point precision issues
- Every fund/debit operation records a **Transaction** with before/after balances
- Transaction history is returned **most recent first**

---

## Design Decisions & Assumptions

1. **BigDecimal for money** — `float`/`double` are unsuitable for financial calculations due to binary floating-point rounding. `BigDecimal` with scale 4 is used throughout.

2. **`@Transactional` on service methods** — Wallet balance updates and transaction log inserts happen atomically. If either fails, the entire operation rolls back.

3. **Transaction history as a bonus feature** — The `GET /transactions/{id}` endpoint returns the full transaction history of a wallet (most recent first), providing an audit trail out of the box.

4. **Single userId per wallet** — The spec does not require unique wallets per user, so a user can have multiple wallets (e.g. for different currencies or purposes).

5. **H2 in-memory storage** — Data is reset on each application restart. For a production system this would be replaced with a persistent database (PostgreSQL, MySQL, etc.) with no code changes required beyond `application.properties`.

6. **No authentication** — Out of scope for this assignment. In production, endpoints would be secured with JWT or OAuth2 (see Follow-Up Discussion below).
