package com.example.automation.pages.orders;

import com.example.automation.utils.BrowserActions;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CreateOrderPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By referenceField = By.id("order_reference");
    private final By customerSearchInput = By.cssSelector("input.customer-search");
    private final By customerDropdownOption = By.cssSelector("li.customer-option.highlighted");
    private final By productSearchInput = By.id("order_product");
    private final By productDropdownOption = By.cssSelector("div.product-option.active");
    private final By approveButton = By.id("save-approve");
    private final By saveDraftButton = By.id("save-draft");

    public CreateOrderPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to create order page")
    public CreateOrderPage navigateToCreateOrder() {
        BrowserActions.navigateV3(driver, System.getProperty("baseUri") + "orders/new");
        return this;
    }

    @Step("Type order reference")
    public CreateOrderPage typeReference(String reference) {
        CustomMethods.setValueForControlledInput(driver, referenceField,
                reference + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Select customer")
    public CreateOrderPage selectCustomer(String customerName) {
        driver.element().type(customerSearchInput, customerName);
        driver.element().doubleClick(customerDropdownOption);
        return this;
    }

    @Step("Select product")
    public CreateOrderPage selectProduct(String productName) {
        driver.element().type(productSearchInput, productName);
        driver.element().click(productDropdownOption);
        return this;
    }

    @Step("Approve order")
    public CreateOrderPage approve() {
        driver.element().scrollToElement(approveButton);
        driver.element().click(approveButton);
        return this;
    }

    @Step("Save order as draft")
    public CreateOrderPage saveAsDraft() {
        driver.element().scrollToElement(saveDraftButton);
        driver.element().click(saveDraftButton);
        return this;
    }
}
