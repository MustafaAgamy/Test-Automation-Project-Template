# Mock App

A self-contained Node.js app used as the application-under-test for the SHAFT Engine template.

## Start locally

```bash
npm install
node server.js
# → http://localhost:3000
```

## Credentials

| Field    | Value               |
|----------|---------------------|
| Email    | demo@example.com    |
| Password | changeme            |
| API key  | demo-api-key        |

## Auth paths

**Path 1 — API login (used by tests):**
The test suite calls `POST /api/auth/login` with JSON credentials.
The server returns `Set-Cookie: session=a1b2c3d4e5f6`.
`BrowserActions.navigateV3()` injects this cookie before each navigation so the browser
never touches the login form.

**Path 2 — Direct bypass:**
`GET /api/auth/token` returns `{"name":"session","value":"a1b2c3d4e5f6"}` with no
credentials required. Inject this cookie manually and any protected page loads immediately.
Example use: quick exploratory testing or CI smoke checks.

## Endpoints

| Method | Path              | Auth | Description                     |
|--------|-------------------|------|---------------------------------|
| POST   | /api/auth/login   | No   | Returns session cookie          |
| GET    | /api/auth/token   | No   | Returns bypass token            |
| GET    | /api/products     | Yes  | Returns products array          |
| POST   | /api/products     | Yes  | Creates product, returns 201    |
| GET    | /products         | Page | Products page (cookie redirect) |
| GET    | /                 | No   | Login page                      |

Auth = cookie `session=a1b2c3d4e5f6` **or** header `API-KEY: demo-api-key`.
