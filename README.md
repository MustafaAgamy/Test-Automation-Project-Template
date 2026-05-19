# Test Automation Project Template

A ready-to-use Java test automation template built on top of [SHAFT Engine](https://github.com/ShaftHQ/SHAFT_ENGINE).
Use it as a starting point for new projects or as a teaching reference for SHAFT Engine patterns.

> **SHAFT Engine User Guide:** https://shafthq.github.io/SHAFT_ENGINE/

---

## What's inside

| Layer | Class | Purpose |
|---|---|---|
| API helper | `ProductsApiHelper` | Create / list products via REST |
| Page objects | `LoginPage`, `CreateProductPage`, `ProductsListPage` | Browser interactions |
| Auth | `SessionHelper`, `BrowserActions` | API login + cookie injection (see Auth Paths below) |
| Tests | `BaseE2ETest` | Suite-level setup — authenticates once, caches session for all tests |
| Tests | `CreateProductApiTest` | API-level tests (group `api`) |
| Tests | `ProductE2ETest` | End-to-end browser tests (group `browser`) |
| Utilities | `TestData`, `CustomMethods` | Shared test data store and helpers |

### Project structure

```
src/
  main/java/com/example/automation/
    apis/
      BaseApiHelper.java          ← RestAssured wrapper base
      auth/SessionHelper.java     ← logs in via API, caches session cookie
      products/ProductsApiHelper.java
    pages/
      LoginPage.java
      products/CreateProductPage.java
      products/ProductsListPage.java
    utils/
      BrowserActions.java         ← navigateV3: injects session cookie before navigation
      CustomMethods.java
      TestData.java               ← thread-safe test data store
  main/resources/properties/
    custom.properties             ← baseUri, credentials, apiKey
    TestNG.properties             ← parallel execution settings
    log4j2.properties

  test/java/com/example/automation/
    tests/
      BaseE2ETest.java                 ← @BeforeSuite: API login, session cache
      api/CreateProductApiTest.java    ← group: api
      e2e/ProductE2ETest.java          ← group: browser
  test/resources/testDataFiles/
    APIs/CreateProduct.json
    Auth/Login.json
    E2E/Product.json

mock-app/                         ← self-contained Node.js target app

.github/
  actions/
    java-setup/action.yml         ← composite: setup-java with Maven cache
    mock-app/action.yml           ← composite: setup-node + start mock app
  workflows/
    tests.yml                     ← CI pipeline
```

---

## Mock app

The `mock-app/` directory contains a self-contained Node.js/Express application that serves as the application under test.
It requires no external services and ships with seed data so tests work immediately.

**Start it locally:**

```bash
cd mock-app
npm install
node server.js
# → http://localhost:3000
```

**Credentials:**

| Field    | Value             |
|----------|-------------------|
| Email    | demo@example.com  |
| Password | changeme          |
| API key  | demo-api-key      |

**Endpoints:**

| Method | Path                          | Auth | Description                                        |
|--------|-------------------------------|------|----------------------------------------------------|
| POST   | /api/auth/login               | No   | Returns `session` cookie                           |
| GET    | /api/auth/token               | No   | Returns bypass token                               |
| GET    | /api/products                 | Yes  | Returns all products                               |
| GET    | /api/products?search=`<name>` | Yes  | Returns products filtered by name (case-insensitive substring match) |
| POST   | /api/products                 | Yes  | Creates product, returns 201                       |
| GET    | /products                     | Page | Products page                                      |
| GET    | /                             | No   | Login page                                         |

Auth = cookie `session=a1b2c3d4e5f6` **or** header `API-KEY: demo-api-key`.

**Products page UI:**

| Element | Selector | Description |
|---------|----------|-------------|
| Search field | `#search-name` | Type a product name to filter by |
| Filter button | `#filter-btn` | Submits the search — also triggered by pressing Enter |
| Create Product button | `#create-product-btn` | Shows the new product form |

---

## Auth paths

**Path 1 — API login (default for tests):**
`SessionHelper.login()` calls `POST /api/auth/login`, caches the `Set-Cookie` header, and `BrowserActions.navigateV3()` injects it as a Selenium cookie before each navigation — the browser never touches the login form.

**Path 2 — Direct bypass:**
`GET /api/auth/token` returns `{"name":"session","value":"a1b2c3d4e5f6"}` with no credentials.
Inject that cookie manually and any protected page loads immediately. Useful for exploratory testing or CI smoke checks.

---

## Running tests locally

**Prerequisites:** Java 25, Maven 3.9+, Node.js 20+

```bash
# 1. Start the mock app
cd mock-app && npm install && node server.js &

# 2. Run API tests (no browser required)
mvn test -Dgroups=api

# 3. Run browser tests (opens a local Chrome window)
mvn test -Dgroups=browser

# 4. Run everything
mvn test
```

**Against a Selenium Grid** (e.g. started via `docker compose up -d`):

```bash
mvn test -Dgroups=browser -DexecutionAddress=http://localhost:4444
```

### Docker Compose (full local stack)

```bash
docker compose up -d          # starts mock-app + selenium-hub + chrome
mvn test -Dgroups=api         # API tests (mock-app at localhost:3000)
docker compose down
```

> **Windows note:** SHAFT Engine 10.2.x has a known issue on Windows where it tries to read the `properties/default/` directory as a file during startup, causing an `AccessDeniedException`. Run tests via Docker on Windows or on Linux/macOS.

---

## CI — GitHub Actions

Three jobs run on every push and pull request:

| Job | Needs | What it does |
|-----|-------|-------------|
| **Compile Check** | — | Checks out and compiles the project; blocks the test jobs if compilation fails |
| **API Tests** | Compile Check | Starts mock app on the runner, runs `mvn test -Dgroups=api` |
| **Browser Tests** | Compile Check | Starts mock app + Selenium Grid in Docker, runs `mvn test -Dgroups=browser` |

The two test jobs run in parallel once the compile check passes.

Allure HTML reports are uploaded as artifacts (`allure-report-api`, `allure-report-browser`) after every run, including failures. Artifacts are retained for 7 days.

The browser job passes the host's primary IP (`hostname -I`) as `baseUri` so Chrome containers inside Docker can reach the mock app running on the runner host.

**Composite actions** (`.github/actions/`) keep the workflow DRY:

| Action | Used by | Steps |
|--------|---------|-------|
| `java-setup` | All three jobs | `actions/checkout` + `actions/setup-java` (JDK 25, Temurin, Maven cache) |
| `mock-app` | API Tests, Browser Tests | `actions/setup-node` (Node 20) + install deps + start server + health check |

Workflow file: [`.github/workflows/tests.yml`](.github/workflows/tests.yml)

---

## Built with

- [SHAFT Engine](https://github.com/ShaftHQ/SHAFT_ENGINE) — Java test automation framework
- [Selenium](https://www.selenium.dev/) — browser automation
- [RestAssured](https://rest-assured.io/) — API testing
- [TestNG](https://testng.org/) — test runner
- [Allure](https://allurereport.org/) — test reporting
- [Express](https://expressjs.com/) — mock app server
