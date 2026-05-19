package com.example.automation.apis.auth;

import com.example.automation.apis.BaseApiHelper;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import lombok.Getter;

/**
 * Authenticates via the login API and caches the session cookie for the test suite run.
 *
 * <p>The cookie is stored in a {@code volatile} static field so it is obtained once
 * per JVM process and reused by all subsequent browser and API tests, avoiding
 * redundant login calls in CI.
 */
public class SessionHelper extends BaseApiHelper {

    @Getter
    private static volatile String sessionCookie;

    public SessionHelper(SHAFT.API api) {
        super(api);
    }

    /**
     * Posts credentials from {@code Auth/Login.json} to {@code /api/auth/login}.
     * No-ops if the session cookie has already been acquired (lazy, single-init).
     * Credentials are injected at runtime via {@code testEmail} and {@code testPassword}
     * system properties.
     */
    @Step("Login")
    public SessionHelper login() {
        if (sessionCookie == null) {
            String body = SHAFT.CLI.file().readFile("src/test/resources/testDataFiles/Auth/Login.json")
                    .replace("{{email}}", System.getProperty("testEmail"))
                    .replace("{{password}}", System.getProperty("testPassword"));
            post("api/auth/login")
                    .setRequestBody(body)
                    .perform();
            sessionCookie = api.getResponse().header("Set-Cookie");
        }
        return this;
    }

    /** Asserts the login response body contains {@code "success"}. */
    @Step("Validate login was successful")
    public SessionHelper validateLoginSuccessful() {
        api.assertThatResponse().body().contains("success");
        return this;
    }
}
