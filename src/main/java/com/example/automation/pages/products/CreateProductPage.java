package com.example.automation.pages.products;

import com.example.automation.utils.BrowserActions;
import com.example.automation.utils.CustomMethods;
import com.example.automation.utils.TestData;
import com.shaft.driver.SHAFT;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class CreateProductPage {

    private final SHAFT.GUI.WebDriver driver;

    private final By createButton   = By.id("create-product-btn");
    private final By nameField      = By.id("product-name");
    private final By skuField       = By.id("product-sku");
    private final By priceField     = By.id("product-price");
    private final By saveButton     = By.id("save-product-btn");
    private final By successMessage = By.cssSelector(".alert-success");

    public CreateProductPage(SHAFT.GUI.WebDriver driver) {
        this.driver = driver;
    }

    @Step("Navigate to products and open create form")
    public CreateProductPage navigateToCreateProduct() {
        BrowserActions.navigateV3(driver, System.getProperty("baseUri") + "products");
        driver.element().click(createButton);
        return this;
    }

    @Step("Type product name")
    public CreateProductPage createProduct(String name, String sku, String price) {
        typeName(name)
            .typeSku(sku)
            .typePrice(price);
        return this;
    }

    @Step("Type product name")
    public CreateProductPage typeName(String name) {
        driver.element().type(nameField, name + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Type product SKU")
    public CreateProductPage typeSku(String sku) {
        driver.element().type(skuField, sku + TestData.getString(TestData.TestDataKey.UUID));
        return this;
    }

    @Step("Type product price")
    public CreateProductPage typePrice(String price) {
        driver.element().type(priceField, price);
        return this;
    }

    @Step("Save product")
    public CreateProductPage save() {
        driver.element().click(saveButton);
        return this;
    }

    @Step("Validate product was created successfully")
    public CreateProductPage validateProductCreatedSuccessfully(String expectedMessage) {
        driver.assertThat().element(successMessage).text().isEqualTo(expectedMessage);
        return this;
    }
}
