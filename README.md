# ModernBank Account Service

ModernBank Account Service is a Spring Boot microservice that manages retail banking accounts, customers, and supporting reference data for the ModernBank school project. It exposes REST APIs for account lifecycle operations, user onboarding, saved beneficiaries, branch and location lookup, and verification flows while integrating with relational storage, Redis caching, and external ModernBank services.

## Features
- **Account management** – Create accounts, list accounts for a user, retrieve account details, look up account owners by IBAN, and update balances through `/api/v1/account` endpoints.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L13-L33】【F:src/main/java/com/modernbank/account_service/rest/controller/AccountServiceController.java†L24-L56】
- **Branch and location directory** – Maintain branch, city, and district data with dedicated CRUD-style APIs that return DTO views for clients.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L13-L29】【F:src/main/java/com/modernbank/account_service/api/CityDistrictControllerApi.java†L12-L21】
- **User profile & saved accounts** – Register and update customers, manage their saved beneficiary accounts, and expose profile information under `/api/v1/user` routes.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L13-L33】【F:src/main/java/com/modernbank/account_service/rest/controller/UserServiceController.java†L24-L60】
- **Verification workflows** – Handle OTP-based verification, cancellation, and resend flows with planned extensions for password reset and admin actions.【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L12-L34】【F:src/main/java/com/modernbank/account_service/rest/controller/VerificationServiceController.java†L22-L45】
- **Cache management & scheduling** – Refresh Redis-backed caches on demand and via scheduled jobs to keep account and error-code data current.【F:src/main/java/com/modernbank/account_service/api/CacheControllerApi.java†L11-L18】【F:src/main/java/com/modernbank/account_service/rest/service/cache/CacheScheduler.java†L15-L24】【F:src/main/java/com/modernbank/account_service/configuration/RedisConfiguration.java†L15-L69】

## Technology Stack
- Java 17 with Spring Boot 3.2 (web, validation, data JPA, caching) and Spring Cloud OpenFeign.【F:pom.xml†L5-L101】
- MySQL for relational persistence via Spring Data repositories.【F:pom.xml†L63-L67】【F:src/main/resources/application.yml†L13-L27】
- Redis (Lettuce) for application caching.【F:pom.xml†L47-L50】【F:src/main/java/com/modernbank/account_service/configuration/RedisConfiguration.java†L15-L69】
- ModelMapper for DTO conversions and Lombok for boilerplate reduction.【F:pom.xml†L52-L56】【F:src/main/java/com/modernbank/account_service/configuration/ApplicationConfiguration.java†L13-L21】
- Optional Kafka dependencies are present for future messaging features.【F:pom.xml†L58-L61】【F:pom.xml†L97-L100】

## Project Layout
```
modernbank_account-service/
├── src/main/java/com/modernbank/account_service
│   ├── api/                # REST interface contracts & DTOs
│   ├── rest/controller/    # HTTP controllers implementing the APIs
│   ├── rest/service/       # Business services and cache schedulers
│   ├── repository/         # Spring Data JPA repositories
│   ├── entity/             # JPA entities for accounts, users, branches, OTP, etc.
│   ├── configuration/      # ModelMapper, Redis, and security beans
│   └── AccountServiceApplication.java  # Spring Boot entry point
├── src/main/resources/application.yml  # Default configuration
└── pom.xml
```

## Configuration
All configuration defaults live in `application.yml` and can be overridden with environment variables or command-line arguments.

| Property | Description | Default |
| --- | --- | --- |
| `spring.datasource.url` | JDBC URL for the MySQL database | `jdbc:mysql://localhost:3306/modern_bank_account_service`【F:src/main/resources/application.yml†L13-L16】 |
| `spring.datasource.username` / `spring.datasource.password` | MySQL credentials | `springstudent` / `springstudent`【F:src/main/resources/application.yml†L13-L16】 |
| `spring.cache.redis.host` / `spring.cache.redis.port` | Redis connection settings | `localhost` / `6379`【F:src/main/resources/application.yml†L20-L24】 |
| `server.port` | HTTP port for the service | `8084`【F:src/main/resources/application.yml†L27-L28】 |
| `cache.refresh.accounts` | Cron expression for scheduled account cache refresh | `0 */5 * * * *` (every 5 minutes)【F:src/main/resources/application.yml†L1-L4】【F:src/main/java/com/modernbank/account_service/rest/service/cache/CacheScheduler.java†L16-L23】 |
| `cache.refresh.cronErrorCode` | Cron for background error-code refresh | `0 0 1 * * SUN` (Sundays at 1 AM)【F:src/main/resources/application.yml†L1-L4】【F:src/main/java/com/modernbank/account_service/rest/service/cache/error/ErrorCacheServiceImpl.java†L44-L51】 |
| `security.encryption.secret-key` | PBKDF2 secret for hashing credentials | _must be supplied_【F:src/main/java/com/modernbank/account_service/configuration/SecurityConfiguration.java†L11-L21】 |
| `feign.client.email-service.*` | Base URL and paths for the email service | `http://localhost:8087/api/v1/email` etc.【F:src/main/resources/application.yml†L31-L36】 |
| `feign.client.parameter-service.*` | Base URL and paths for the parameter service | `http://localhost:8086/api/v1` etc.【F:src/main/resources/application.yml†L37-L41】 |

> **Security note:** Security auto-configuration is disabled, but a PBKDF2 encoder bean requires `security.encryption.secret-key` to be provided for password hashing.【F:src/main/java/com/modernbank/account_service/AccountServiceApplication.java†L9-L17】【F:src/main/java/com/modernbank/account_service/configuration/SecurityConfiguration.java†L11-L21】

## Getting Started
1. **Install prerequisites**
   - Java 17
   - Maven 3.9+
   - MySQL 8.x with an empty schema named `modern_bank_account_service`.
   - Redis 7.x
2. **Configure environment**
   - Update `src/main/resources/application.yml` or export environment variables for database credentials, Redis host/port, Feign client URLs, and `security.encryption.secret-key`.
3. **Build and run**
   ```bash
   mvn clean package
   java -jar target/account-service-0.0.1-SNAPSHOT.jar
   ```
   The service starts on port `8084` by default.【F:src/main/resources/application.yml†L27-L28】
4. **Access the APIs**
   - Base URL: `http://localhost:8084/api/v1`
   - Use an HTTP client (Postman, curl) to call the endpoints listed below. Include required headers such as `X-User-Id` where specified.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L17-L21】

## Key API Endpoints
All endpoints are JSON-based; request/response schemas are defined in the `api.dto`, `api.request`, and `api.response` packages.

| Method & Path | Description |
| --- | --- |
| `POST /account/create` | Create a new account for a user.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L13-L16】 |
| `GET /account/get` | List accounts for the authenticated or specified user (`X-User-Id` header).【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L17-L20】 |
| `POST /account/get-details` | Fetch detailed information for an account by ID/IBAN payload.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L21-L22】 |
| `POST /account/getv2` | Alternate account listing using request body user context.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L23-L24】 |
| `GET /account/get-by-iban` | Retrieve account summary by IBAN query parameter.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L25-L26】 |
| `GET /account/get/user/by-iban` | Resolve the account owner’s name by IBAN.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L27-L28】 |
| `POST /account/balance/update` | Update an account balance by IBAN and amount.【F:src/main/java/com/modernbank/account_service/api/AccountControllerApi.java†L29-L33】 |
| `POST /branch/create` | Create a bank branch record.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L13-L16】 |
| `PUT /branch/update` | Update branch metadata.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L17-L18】 |
| `GET /branch/cities` | Retrieve active branch cities.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L19-L20】 |
| `GET /branch/districts?city=` | Fetch districts by city ID.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L21-L22】 |
| `GET /branch/branches?district=` | Fetch active branches by district ID.【F:src/main/java/com/modernbank/account_service/api/BranchControllerApi.java†L23-L24】 |
| `POST /city-districts/city/create` | Create a city definition.【F:src/main/java/com/modernbank/account_service/api/CityDistrictControllerApi.java†L12-L15】 |
| `POST /city-districts/district/create` | Create a district definition.【F:src/main/java/com/modernbank/account_service/api/CityDistrictControllerApi.java†L16-L17】 |
| `PUT /city-districts/district/update` | Update district metadata.【F:src/main/java/com/modernbank/account_service/api/CityDistrictControllerApi.java†L18-L19】 |
| `PUT /city-districts/city/update` | Update city metadata.【F:src/main/java/com/modernbank/account_service/api/CityDistrictControllerApi.java†L20-L21】 |
| `POST /user/create` | Register a new customer profile.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L13-L16】 |
| `POST /user/update` | Update customer profile fields (implementation pending).【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L17-L18】【F:src/main/java/com/modernbank/account_service/rest/controller/UserServiceController.java†L32-L35】 |
| `POST /user/saved-accounts/add` | Add a saved beneficiary account for quick transfers.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L19-L22】 |
| `POST /user/saved-accounts/get` | List saved beneficiary accounts for a user.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L23-L24】 |
| `POST /user/saved-accounts/remove` | Remove a saved beneficiary account.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L25-L26】 |
| `POST /user/get/info` | Retrieve user profile data by email or ID payload.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L27-L28】 |
| `POST /user/update/info` | Update user info using request body and `X-User-Id` header.【F:src/main/java/com/modernbank/account_service/api/UserControllerApi.java†L29-L31】【F:src/main/java/com/modernbank/account_service/rest/controller/UserServiceController.java†L52-L60】 |
| `POST /verification/user/verify` | Verify a user with an OTP code.【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L12-L15】 |
| `POST /verification/user/verify/cancel` | Cancel an ongoing verification flow.【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L16-L17】 |
| `POST /verification/user/verify/resend` | Resend verification OTP email.【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L18-L19】 |
| `POST /verification/user/password/send-reset-otp` | Request an OTP for password reset (implementation pending).【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L21-L24】【F:src/main/java/com/modernbank/account_service/rest/controller/VerificationServiceController.java†L39-L45】 |
| `POST /verification/user/password/verify-user-otp` | Verify the reset OTP (implementation pending).【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L23-L26】【F:src/main/java/com/modernbank/account_service/rest/controller/VerificationServiceController.java†L39-L45】 |
| `POST /verification/user/password/reset` | Complete the password reset (implementation pending).【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L26-L27】【F:src/main/java/com/modernbank/account_service/rest/controller/VerificationServiceController.java†L39-L45】 |
| `POST /verification/verify-by-admin` | Future admin verification endpoint (TODO).【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L29-L31】 |
| `POST /verification/unverify-by-admin` | Future admin unverify endpoint (TODO).【F:src/main/java/com/modernbank/account_service/api/VerificationControllerApi.java†L31-L33】 |
| `POST /cache/account/refresh` | Trigger account cache refresh across Redis.【F:src/main/java/com/modernbank/account_service/api/CacheControllerApi.java†L11-L12】 |
| `POST /cache/city/refresh` | Refresh cached city data.【F:src/main/java/com/modernbank/account_service/api/CacheControllerApi.java†L13-L14】 |
| `POST /cache/district/refresh` | Refresh cached district data.【F:src/main/java/com/modernbank/account_service/api/CacheControllerApi.java†L15-L16】 |

## Data Model Highlights
- `Account`, `User`, `Branch`, `City`, and `District` entities form the core relational model with relationships defined via JPA annotations.【F:src/main/java/com/modernbank/account_service/entity/Account.java†L13-L48】【F:src/main/java/com/modernbank/account_service/entity/User.java†L1-L80】
- `OTPCode` captures verification codes, while `ErrorCodes` enables cached error lookups consumed through Feign clients and Redis.【F:src/main/java/com/modernbank/account_service/entity/OTPCode.java†L1-L68】【F:src/main/java/com/modernbank/account_service/configuration/RedisConfiguration.java†L35-L50】
- Repositories in `repository/` expose CRUD access for each entity leveraging Spring Data JPA.【F:src/main/java/com/modernbank/account_service/repository/AccountRepository.java†L1-L40】

## Development Notes
- ModelMapper is configured with strict matching and null-skipping to ensure DTO conversion accuracy.【F:src/main/java/com/modernbank/account_service/configuration/ApplicationConfiguration.java†L13-L21】
- Caching uses a custom key generator (`ClassName:method:param1:…`) to avoid key collisions across services.【F:src/main/java/com/modernbank/account_service/configuration/RedisConfiguration.java†L52-L68】
- Scheduled cache refresh logs outcomes and gracefully handles exceptions to avoid crashing the scheduler thread.【F:src/main/java/com/modernbank/account_service/rest/service/cache/CacheScheduler.java†L17-L24】
- Security auto-configuration is disabled; custom authentication/authorization can be layered without conflicting with Spring Security defaults.【F:src/main/java/com/modernbank/account_service/AccountServiceApplication.java†L9-L17】
