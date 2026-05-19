package com.example.automation.utils;

import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Shared browser utilities and test-data helpers that don't belong to a specific page object.
 */
public class CustomMethods {

    private CustomMethods() {
    }

    /**
     * Returns a compact numeric string unique enough for test data names.
     * Combines epoch-millis (mod 1B) with a two-digit random suffix to avoid
     * collisions when tests run in parallel or in rapid succession.
     */
    public static String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() % 1_000_000_000L) +
                ThreadLocalRandom.current().nextInt(10, 99);
    }

    /**
     * Sets a value on a React (controlled) input by writing directly to the DOM
     * via JavaScript, bypassing React's synthetic event system which normally
     * prevents programmatic {@code .value} assignment from triggering state updates.
     *
     * @param driver  the SHAFT WebDriver instance
     * @param locator the controlled input element
     * @param input   the value to set
     */
    @Step("Type Into Controlled Input")
    public static void setValueForControlledInput(SHAFT.GUI.WebDriver driver, By locator, String input) {
        driver.element().waitUntil(webDriver -> driver.element().get().isDisplayed(locator));
        ((JavascriptExecutor) driver.getDriver()).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].setAttribute('value', arguments[1]);",
                driver.getDriver().findElement(locator), input);
    }
}
