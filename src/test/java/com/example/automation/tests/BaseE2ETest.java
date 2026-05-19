package com.example.automation.tests;

import com.example.automation.apis.auth.SessionHelper;
import com.shaft.driver.SHAFT;
import org.testng.annotations.BeforeSuite;

/**
 * Base class for all E2E tests. Performs suite-level setup that runs once before
 * any test in the suite, regardless of which test class triggers it.
 *
 * <p>Subclasses inherit the authenticated session established here — no individual
 * test class needs to handle login directly.
 */
public class BaseE2ETest {

    /**
     * Authenticates once per suite via the API and caches the session cookie in
     * {@link SessionHelper}. All subsequent browser and API calls reuse this cookie,
     * avoiding redundant login requests across test classes.
     */
    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        new SessionHelper(new SHAFT.API(System.getProperty("baseUri"))).login();
    }
}
