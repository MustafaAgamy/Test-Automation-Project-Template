package com.example.automation.pages.products;

import com.example.automation.utils.BrowserActions;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class ProductsListPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By statusBadge = By.cssSelector("td.product-status span");

    public ProductsListPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to products list")
    public ProductsListPage navigateToProductsList() {
        BrowserActions.navigateV3(driver, System.getProperty("baseUri") + "products");
        return this;
    }

    @Step("Search for product")
    public ProductsListPage searchForProduct(String name) {
        CustomMethods.searchForRecord(driver,
                System.getProperty("baseUri") + "products?search=",
                name + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Validate product status is {expectedStatus}")
    public ProductsListPage validateProductStatus(String expectedStatus) {
        driver.assertThat().element(statusBadge).text().isEqualTo(expectedStatus);
        return this;
    }
}
