package com.example.automation.pages.customers;

import com.example.automation.utils.BrowserActions;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CreateCustomerPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By nameField = By.id("customer_name");
    private final By emailField = By.id("customer_email");
    private final By saveButton = By.cssSelector("button.save-customer");
    private final By successMessage = By.cssSelector(".alert-success");

    public CreateCustomerPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to create customer page")
    public CreateCustomerPage navigateToCreateCustomer() {
        BrowserActions.navigateV3(driver, System.getProperty("baseUri") + "customers/new");
        return this;
    }

    @Step("Type customer name")
    public CreateCustomerPage typeName(String name) {
        driver.element().type(nameField, name + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Type customer email")
    public CreateCustomerPage typeEmail(String email) {
        driver.element().type(emailField,
                TestData.getString(TestData.TestDataKey.UUID) + email);
        return this;
    }

    @Step("Save customer")
    public CreateCustomerPage save() {
        driver.element().click(saveButton);
        return this;
    }

    @Step("Validate customer was created successfully")
    public CreateCustomerPage validateCreatedSuccessfully(String expectedMessage) {
        driver.assertThat().element(successMessage).text().isEqualTo(expectedMessage);
        return this;
    }
}
