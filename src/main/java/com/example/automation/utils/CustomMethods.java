package com.example.automation.utils;

import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

public class CustomMethods {

    private CustomMethods() {
    }

    public static String generateTimeStamp() {
        return String.valueOf(System.currentTimeMillis() % 1_000_000_000L) +
                ThreadLocalRandom.current().nextInt(10, 99);
    }

    public static void setValueForControlledInput(SHAFT.GUI.WebDriver driver, By locator, String input) {
        driver.element().waitUntil(webDriver -> driver.element().get().isDisplayed(locator));
        ((JavascriptExecutor) driver.getDriver()).executeScript(
                "arguments[0].value = arguments[1]; arguments[0].setAttribute('value', arguments[1]);",
                driver.getDriver().findElement(locator), input);
    }

    @Step("Search for Record")
    public static void searchForRecord(SHAFT.GUI.WebDriver driver, String baseSearchUrl, String record) {
        for (int attempt = 0; attempt < 3; attempt++) {
            driver.browser().navigateToURL(baseSearchUrl + record);
            try {
                new WebDriverWait(driver.getDriver(), Duration.ofSeconds(10))
                        .until(d -> !d.findElements(
                                By.xpath("//*[contains(normalize-space(), '" + record + "')]")).isEmpty());
                return;
            } catch (TimeoutException ignored) {
                // retry
            }
        }
    }

    @Step("Verify Record is Not Found")
    public static void searchForDeletedRecord(SHAFT.GUI.WebDriver driver, String baseSearchUrl,
                                              String record, String noResultsMessage) {
        for (int attempt = 0; attempt < 3; attempt++) {
            driver.browser().navigateToURL(baseSearchUrl + record);
            try {
                new WebDriverWait(driver.getDriver(), Duration.ofSeconds(10))
                        .until(d -> !d.findElements(
                                By.xpath("//*[contains(normalize-space(), '" + noResultsMessage + "')]")).isEmpty());
                return;
            } catch (TimeoutException ignored) {
                // retry
            }
        }
    }
}
