package com.example.automation.utils;

import com.example.automation.apis.auth.SessionHelper;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.Cookie;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class BrowserActions {

    private BrowserActions() {
    }

    /**
     * Navigates to a protected URL by injecting the session cookie obtained via
     * {@link com.example.automation.apis.auth.SessionHelper#login()} (Path 1 — API login).
     *
     * <p>Bypass alternative (Path 2): call {@code GET /api/auth/token} to receive
     * {@code {"name":"session","value":"..."}} — inject that cookie directly and
     * navigate without any credentials. Useful for exploratory testing or CI smoke checks.
     */
    @Step("Navigate with Authentication")
    public static BrowserActions navigateV3(SHAFT.GUI.WebDriver driver, String url) {
        driver.browser().navigateToURL(System.getProperty("baseUri"));
        driver.browser().deleteAllCookies();
        driver.element().waitUntil(webDriver ->
                webDriver.getCurrentUrl().contains(System.getProperty("baseUri")));
        driver.getDriver().manage().addCookie(buildCookie(SessionHelper.getSessionCookie()));
        driver.browser().navigateToURL(url);
        return new BrowserActions();
    }

    private static Cookie buildCookie(String rawCookieHeader) {
        String[] parts = rawCookieHeader.split(";");
        String[] nameValue = parts[0].split("=", 2);

        String name = nameValue[0].trim();
        String value = nameValue.length > 1 ? nameValue[1].trim() : "";

        Map<String, String> attributes = Arrays.stream(parts)
                .skip(1)
                .map(String::trim)
                .filter(part -> !part.isEmpty())
                .collect(Collectors.toMap(
                        part -> part.split("=")[0].trim().toLowerCase(),
                        part -> {
                            String[] kv = part.split("=", 2);
                            return kv.length > 1 ? kv[1].trim() : "";
                        },
                        (a, b) -> a));

        Cookie.Builder builder = new Cookie.Builder(name, value)
                .path(attributes.getOrDefault("path", "/"));

        if (attributes.containsKey("domain")) builder.domain(attributes.get("domain"));
        if (attributes.containsKey("secure")) builder.isSecure(true);
        if (attributes.containsKey("httponly")) builder.isHttpOnly(true);

        return builder.build();
    }
}
