package com.example.automation.pages.orders;

import com.example.automation.utils.BrowserActions;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class OrdersListPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By statusBadge = By.cssSelector("td.order-status span");
    private final By noResultsMessage = By.cssSelector(".no-results-found");

    public OrdersListPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to orders list")
    public OrdersListPage navigateToOrdersList() {
        BrowserActions.navigateV3(driver, System.getProperty("baseUri") + "orders");
        return this;
    }

    @Step("Search for order by reference")
    public OrdersListPage searchForOrder(String reference) {
        CustomMethods.searchForRecord(driver,
                System.getProperty("baseUri") + "orders?search=",
                reference + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Validate order status is {expectedStatus}")
    public OrdersListPage validateOrderStatus(String expectedStatus) {
        driver.assertThat().element(statusBadge).text().isEqualTo(expectedStatus);
        return this;
    }

    @Step("Validate order does not exist")
    public OrdersListPage validateOrderNotFound(String message) {
        driver.assertThat().element(noResultsMessage).text().isEqualTo(message);
        return this;
    }
}
